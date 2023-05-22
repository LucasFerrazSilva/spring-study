package br.com.ferraz.springstudy.domain.doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Page<Doctor> findAllByActiveIsTrue(Pageable pageable);

    Optional<Doctor> findByIdAndActiveIsTrue(Long doctorId);

    @Query("""
            SELECT d FROM Doctor d
            WHERE
                d.active = 1 
                AND d.expertise = :expertise
                AND d.id NOT IN (
                    SELECT a.doctor.id FROM Appointment a WHERE a.appointmentTime = :appointmentTime
                )
            ORDER BY
                rand()
            LIMIT 1 
            """)
    Optional<Doctor> findRandomFreeDoctorAtTheAppointmentTimeWithExpertise(LocalDateTime appointmentTime, Expertise expertise);
}
