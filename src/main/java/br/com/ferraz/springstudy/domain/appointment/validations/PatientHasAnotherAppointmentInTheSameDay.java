package br.com.ferraz.springstudy.domain.appointment.validations;

import br.com.ferraz.springstudy.domain.appointment.AppointmentRepository;
import br.com.ferraz.springstudy.domain.appointment.NewAppointmentDTO;
import br.com.ferraz.springstudy.infra.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientHasAnotherAppointmentInTheSameDay implements NewAppointmentValidator {

    @Autowired
    private AppointmentRepository repository;

    public void validate(NewAppointmentDTO dto) {
        boolean existsAnotherAppointment =
                repository.existsByPatientIdAndAppointmentTimeBetweenAndActiveIsTrue(dto.patientId(), dto.appointmentTime().withHour(0), dto.appointmentTime().withHour(23));

        if (existsAnotherAppointment)
            throw new ValidationException("appointmentTime", "O paciente já possui uma consulta para o dia especificado.");
    }

}
