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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="patient_id")
    private Patient patient;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="doctor_id")
    private Doctor doctor;
    private LocalDateTime appointmentTime;
    @Column(nullable = false, columnDefinition = "TINYINT")
    private Boolean active;
    @Enumerated(EnumType.STRING)
    private ReasonForCancellation reasonForCancellation;


    public Appointment(Patient patient, Doctor doctor, LocalDateTime appointmentTime) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentTime = appointmentTime;
        this.active = true;
    }

    public String getPatientName() {
        return this.patient.getName();
    }

    public String getDoctorName() {
        return this.doctor.getName();
    }

    public String getDoctorExpertise() {
        return this.doctor.getExpertise().toString();
    }

    public boolean canCancel() {
        return LocalDateTime.now().isBefore(this.appointmentTime.minusHours(24));
    }

    public void cancel(ReasonForCancellation reasonForCancellation){
        this.active = false;
        this.reasonForCancellation = reasonForCancellation;
    }
}
