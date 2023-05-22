package br.com.ferraz.springstudy.controller;

import br.com.ferraz.springstudy.domain.appointment.*;
import br.com.ferraz.springstudy.domain.doctor.Doctor;
import br.com.ferraz.springstudy.domain.doctor.DoctorRepository;
import br.com.ferraz.springstudy.domain.patient.Patient;
import br.com.ferraz.springstudy.domain.patient.PatientRepository;
import br.com.ferraz.springstudy.infra.exception.ValidationException;
import br.com.ferraz.springstudy.infra.exception.ValidationExceptionDataDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/consultas")
public class AppointmentController {

    private AppointmentRepository repository;
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;

    public AppointmentController(AppointmentRepository repository, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.repository = repository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity save(@RequestBody @Valid NewAppointmentDTO dto, UriComponentsBuilder uriBuilder) {
        dto = new NewAppointmentDTO(dto.patientId(), dto.doctorId(), dto.appointmentTime().withMinute(0).withSecond(0));
        Appointment appointment = validateDTO(dto);
        repository.save(appointment);

        URI uri = uriBuilder.path("/consultas/{id}").buildAndExpand(appointment.getId()).toUri();
        AppointmentInfosDTO infosDTO = new AppointmentInfosDTO(appointment);

        return ResponseEntity.created(uri).body(infosDTO);
    }

    private Appointment validateDTO(NewAppointmentDTO dto) {
        Patient patient = patientRepository.findByIdAndActiveIsTrue(dto.patientId())
                .orElseThrow(() -> new ValidationException("patientId", "Nenhum paciente ativo encontrado para o ID fornecido"));

        boolean isInvalidDateTime = dto.appointmentTime().getDayOfWeek().equals(DayOfWeek.SUNDAY)
                || dto.appointmentTime().toLocalTime().isBefore(LocalTime.of(7, 0, 0))
                || dto.appointmentTime().toLocalTime().isAfter(LocalTime.of(19, 0, 0));
        if (isInvalidDateTime)
            throw new ValidationException("appointmentTime", "Dia/Horário inválido (somente de segunda à sábado das 7 às 19)");

        boolean isBefore30MinutesFromNow = dto.appointmentTime().minusMinutes(30).isBefore(LocalDateTime.now());
        if(isBefore30MinutesFromNow)
            throw new ValidationException("appointmentTime", "Não é possível realizar o agendamento com menos de 30 minutos de antecedência.");

        boolean existsAppointmentForThePatientInTheSameDay =
                repository.existsByPatientAndAppointmentTimeBetween(patient, dto.appointmentTime().withHour(0), dto.appointmentTime().withHour(23));
        if (existsAppointmentForThePatientInTheSameDay)
            throw new ValidationException("appointmentTime", "O paciente já possui uma consulta para o dia especificado.");

        Doctor doctor = null;
        if (dto.doctorId() != null) {
            doctor = doctorRepository.findByIdAndActiveIsTrue(dto.doctorId());
            if (doctor == null)
                throw new ValidationException("doctorId", "Nenhum médico ativo encontrado para o ID fornecido");

            boolean existsAppointmentForTheDoctorAtTheSameTime = repository.existsByDoctorAndAppointmentTime(doctor, dto.appointmentTime());
            if (existsAppointmentForTheDoctorAtTheSameTime)
                throw new ValidationException("appointmentTime", "O médico já possui uma consulta nesse dia/hora.");
        } else {
            List<Appointment> appointments = repository.findByAppointmentTime(dto.appointmentTime());
            List<Long> occupiedDoctors = appointments.stream().map(Appointment::getDoctorId).toList();
            doctor = doctorRepository.findFirstByIdNotInAndActiveIsTrue(occupiedDoctors);
            if (doctor == null)
                throw new ValidationException("appointmentTime", "Nenhum médico disponível para o horário especificado.");
        }

        Appointment appointment = new Appointment(patient, doctor, dto.appointmentTime());
        return appointment;
    }

    // Read (List, Get)
    @GetMapping
    public ResponseEntity<Page> list(@PageableDefault Pageable pageable) {
        Page<Appointment> list = repository.findAll(pageable);
        Page<AppointmentListDTO> listDTOs = list.map(AppointmentListDTO::new);

        return ResponseEntity.ok(listDTOs);
    }

    // Update
    // Delete

}
