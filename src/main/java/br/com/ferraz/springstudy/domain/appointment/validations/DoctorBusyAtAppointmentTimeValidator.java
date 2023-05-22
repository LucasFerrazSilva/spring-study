package br.com.ferraz.springstudy.domain.appointment.validations;

import br.com.ferraz.springstudy.domain.appointment.AppointmentRepository;
import br.com.ferraz.springstudy.domain.appointment.NewAppointmentDTO;
import br.com.ferraz.springstudy.domain.doctor.Doctor;
import br.com.ferraz.springstudy.infra.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DoctorBusyAtAppointmentTimeValidator implements NewAppointmentValidator {

    @Autowired
    private AppointmentRepository repository;

    public void validate(NewAppointmentDTO dto) {
        boolean existsAppointment = repository.existsByDoctorIdAndAppointmentTimeAndActiveIsTrue(dto.doctorId(), dto.appointmentTime());

        if (existsAppointment)
            throw new ValidationException("appointmentTime", "O médico já possui uma consulta nesse dia/hora.");
    }

}
