package br.com.ferraz.springstudy.domain.appointment;

import br.com.ferraz.springstudy.domain.doctor.Doctor;
import br.com.ferraz.springstudy.domain.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Boolean existsByPatientAndAppointmentTimeBetweenAndActiveIsTrue(Patient patient, LocalDateTime start, LocalDateTime end);

    Boolean existsByDoctorAndAppointmentTimeAndActiveIsTrue(Doctor doctor, LocalDateTime appointmentTime);

    Page<Appointment> findByActiveIsTrue(Pageable pageable);
}
