package br.com.ferraz.springstudy.domain.appointment;

import br.com.ferraz.springstudy.domain.appointment.validations.NewAppointmentValidator;
import br.com.ferraz.springstudy.domain.doctor.Doctor;
import br.com.ferraz.springstudy.domain.doctor.DoctorRepository;
import br.com.ferraz.springstudy.domain.patient.Patient;
import br.com.ferraz.springstudy.domain.patient.PatientRepository;
import br.com.ferraz.springstudy.infra.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private AppointmentRepository repository;
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;

    @Autowired // O Spring busca todas os componentes que implementam essa interface e injeta automaticamente na lista
    private List<NewAppointmentValidator> validators;

    public AppointmentService(AppointmentRepository repository, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.repository = repository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public Appointment scheduleAppointment(NewAppointmentDTO dto) {
        Patient patient = getPatient(dto);
        Doctor doctor = getDoctor(dto);
        validators.forEach(validator -> validator.validate(dto));
        Appointment appointment = new Appointment(patient, doctor, dto.appointmentTime());
        repository.save(appointment);
        return appointment;
    }

    public Appointment validateDTO(NewAppointmentDTO dto) {
        Patient patient = getPatient(dto);
        Doctor doctor = getDoctor(dto);

        validators.forEach(validator -> validator.validate(dto));

        Appointment appointment = new Appointment(patient, doctor, dto.appointmentTime());
        return appointment;
    }

    private Patient getPatient(NewAppointmentDTO dto) {
        return patientRepository.findByIdAndActiveIsTrue(dto.patientId())
                .orElseThrow(() -> new ValidationException("patientId", "Nenhum paciente ativo encontrado para o ID fornecido"));
    }

    private Doctor getDoctor(NewAppointmentDTO dto) {
        if (dto.doctorId() != null) {
            Doctor doctor = doctorRepository.findByIdAndActiveIsTrue(dto.doctorId())
                    .orElseThrow(() -> new ValidationException("doctorId", "Nenhum médico ativo encontrado para o ID fornecido."));

            return doctor;
        } else if (dto.expertise() != null) {
            return doctorRepository.findRandomFreeDoctorAtTheAppointmentTimeWithExpertise(dto.appointmentTime(), dto.expertise())
                    .orElseThrow(() -> new ValidationException("appointmentTime", "Nenhum médico disponível para o horário especificado."));
        } else {
            throw new ValidationException("doctorId", "Favor informar o médico ou a especialidade desejada.");
        }
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
