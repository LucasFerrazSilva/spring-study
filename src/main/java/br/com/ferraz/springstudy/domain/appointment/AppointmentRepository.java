package br.com.ferraz.springstudy.domain.appointment;

import br.com.ferraz.springstudy.domain.doctor.Doctor;
import br.com.ferraz.springstudy.domain.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Boolean existsByPatientAndAppointmentTimeBetween(Patient patient, LocalDateTime start, LocalDateTime end);

    Boolean existsByDoctorAndAppointmentTime(Doctor doctor, LocalDateTime appointmentTime);

    List<Appointment> findByAppointmentTime(LocalDateTime appointmentTime);

}
