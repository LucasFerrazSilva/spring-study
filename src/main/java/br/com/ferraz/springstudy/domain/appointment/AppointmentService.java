package br.com.ferraz.springstudy.domain.appointment;

import br.com.ferraz.springstudy.controller.CancelAppointmentDTO;
import br.com.ferraz.springstudy.domain.doctor.Doctor;
import br.com.ferraz.springstudy.domain.doctor.DoctorRepository;
import br.com.ferraz.springstudy.domain.patient.Patient;
import br.com.ferraz.springstudy.domain.patient.PatientRepository;
import br.com.ferraz.springstudy.infra.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class AppointmentService {

    private AppointmentRepository repository;
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository repository, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.repository = repository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public Appointment scheduleAppointment(NewAppointmentDTO dto) {
        dto = new NewAppointmentDTO(dto.patientId(), dto.doctorId(), dto.expertise(), dto.appointmentTime().withMinute(0).withSecond(0));
        Appointment appointment = validateDTO(dto);
        repository.save(appointment);

        return appointment;
    }

    public Appointment validateDTO(NewAppointmentDTO dto) {
        Patient patient = validateAndGetPatient(dto);
        Doctor doctor = validateAndGetDoctor(dto);

        if (isOutsideOpeningHours(dto))
            throw new ValidationException("appointmentTime", "Dia/Horário inválido (somente de segunda à sábado das 7 às 19)");

        if(isBefore30MinutesFromNow(dto))
            throw new ValidationException("appointmentTime", "Não é possível realizar o agendamento com menos de 30 minutos de antecedência.");

        if (patientHasAppointmentInTheSameDay(dto, patient))
            throw new ValidationException("appointmentTime", "O paciente já possui uma consulta para o dia especificado.");

        Appointment appointment = new Appointment(patient, doctor, dto.appointmentTime());
        return appointment;
    }

    private Patient validateAndGetPatient(NewAppointmentDTO dto) {
        return patientRepository.findByIdAndActiveIsTrue(dto.patientId())
                .orElseThrow(() -> new ValidationException("patientId", "Nenhum paciente ativo encontrado para o ID fornecido"));
    }

    private Doctor validateAndGetDoctor(NewAppointmentDTO dto) {
        if (dto.doctorId() != null) {
            Doctor doctor = doctorRepository.findByIdAndActiveIsTrue(dto.doctorId())
                    .orElseThrow(() -> new ValidationException("doctorId", "Nenhum médico ativo encontrado para o ID fornecido."));

            if (existsAppointmentForTheDoctorAtTheSameTime(dto, doctor))
                throw new ValidationException("appointmentTime", "O médico já possui uma consulta nesse dia/hora.");

            return doctor;
        } else if (dto.expertise() != null) {
            return doctorRepository.findRandomFreeDoctorAtTheAppointmentTimeWithExpertise(dto.appointmentTime(), dto.expertise())
                    .orElseThrow(() -> new ValidationException("appointmentTime", "Nenhum médico disponível para o horário especificado."));
        } else {
            throw new ValidationException("doctorId", "Favor informar o médico ou a especialidade desejada.");
        }
    }

    private boolean existsAppointmentForTheDoctorAtTheSameTime(NewAppointmentDTO dto, Doctor doctor) {
        return repository.existsByDoctorAndAppointmentTime(doctor, dto.appointmentTime());
    }

    private boolean patientHasAppointmentInTheSameDay(NewAppointmentDTO dto, Patient patient) {
        return repository.existsByPatientAndAppointmentTimeBetween(patient, dto.appointmentTime().withHour(0), dto.appointmentTime().withHour(23));
    }

    private boolean isBefore30MinutesFromNow(NewAppointmentDTO dto) {
        return dto.appointmentTime().minusMinutes(30).isBefore(LocalDateTime.now());
    }

    private boolean isOutsideOpeningHours(NewAppointmentDTO dto) {
        return dto.appointmentTime().getDayOfWeek().equals(DayOfWeek.SUNDAY)
                || dto.appointmentTime().toLocalTime().isBefore(LocalTime.of(7, 0, 0))
                || dto.appointmentTime().toLocalTime().isAfter(LocalTime.of(19, 0, 0));
    }

    public void cancelAppointment(CancelAppointmentDTO dto) {
        Appointment appointment = repository.findById(dto.id())
                .orElseThrow(() -> new ValidationException("id", "Nenhuma consulta encontrada para o ID fornecido."));

        if (!appointment.canCancel()) {
            throw new ValidationException("id", "Não é possível cancelar a consulta com menos de 24 horas de antecedência");
        }

        appointment.cancel(dto.reasonForCancellation());
    }
}
