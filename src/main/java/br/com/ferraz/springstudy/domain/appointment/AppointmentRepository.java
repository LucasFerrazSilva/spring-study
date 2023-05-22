package br.com.ferraz.springstudy.domain.appointment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Boolean existsByPatientIdAndAppointmentTimeBetweenAndActiveIsTrue(Long patientId, LocalDateTime start, LocalDateTime end);

    Boolean existsByDoctorIdAndAppointmentTimeAndActiveIsTrue(Long doctorId, LocalDateTime appointmentTime);

    Page<Appointment> findByActiveIsTrue(Pageable pageable);
}
