package br.com.ferraz.springstudy.domain.appointment;

import br.com.ferraz.springstudy.domain.doctor.Doctor;
import br.com.ferraz.springstudy.domain.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="TB_APPOINTMENTS")
@Getter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of="id") @ToString
public class Appointment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Doctor doctor;

    private LocalDateTime appointmentTime;

}
