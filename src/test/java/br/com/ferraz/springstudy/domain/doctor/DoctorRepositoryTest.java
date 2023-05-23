package br.com.ferraz.springstudy.domain.doctor;

import br.com.ferraz.springstudy.domain.address.AddressDTO;
import br.com.ferraz.springstudy.domain.appointment.Appointment;
import br.com.ferraz.springstudy.domain.appointment.ReasonForCancellation;
import br.com.ferraz.springstudy.domain.patient.NewPatientDTO;
import br.com.ferraz.springstudy.domain.patient.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Deve retornar null quando unico medico cadastrado nao esta disponível na data")
    void testFindRandomFreeDoctorAtTheAppointmentTimeWithExpertiseCase1() {
        // Given
        LocalDateTime mondayAt10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
        var doctor = createDoctor();
        var patient = createPatient();
        createAppointment(doctor, patient, mondayAt10);

        // When
        Doctor freeDoctor = repository.findRandomFreeDoctorAtTheAppointmentTimeWithExpertise(mondayAt10, Expertise.CARDIOLOGIA).orElse(null);

        // Then
        assertThat(freeDoctor).isNull();
    }

    @Test
    @DisplayName("Deve retornar medico1 quando ele estiver disponivel na data")
    void testFindRandomFreeDoctorAtTheAppointmentTimeWithExpertiseCase2() {
        // Given
        LocalDateTime mondayAt10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
        Doctor doctor = createDoctor();

        // When
        Doctor freeDoctor = repository.findRandomFreeDoctorAtTheAppointmentTimeWithExpertise(mondayAt10, Expertise.CARDIOLOGIA).orElse(null);

        // Then
        assertThat(freeDoctor).isEqualTo(doctor);
    }

    @Test
    @DisplayName("Deve retornar medico2 quando ele estiver disponivel na data e medico1 estiver ocupado")
    void testFindRandomFreeDoctorAtTheAppointmentTimeWithExpertiseCase3() {
        // Given
        LocalDateTime mondayAt10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
        var doctor1 = createDoctor();
        var patient = createPatient();
        createAppointment(doctor1, patient, mondayAt10);
        var doctor2 = createDoctor("medico2", Expertise.CARDIOLOGIA);

        // When
        Doctor freeDoctor = repository.findRandomFreeDoctorAtTheAppointmentTimeWithExpertise(mondayAt10, Expertise.CARDIOLOGIA).orElse(null);

        // Then
        assertThat(freeDoctor).isEqualTo(doctor2);
    }

    @Test
    @DisplayName("Deve retornar null quando medico1 estiver ocupado, medico2 livre mas medico2 for de outra especialidade")
    void testFindRandomFreeDoctorAtTheAppointmentTimeWithExpertiseCase4() {
        // Given
        LocalDateTime mondayAt10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
        var doctor1 = createDoctor();
        var patient = createPatient();
        createAppointment(doctor1, patient, mondayAt10);
        createDoctor("medico2", Expertise.DERMATOLOGIA);

        // When
        Doctor freeDoctor = repository.findRandomFreeDoctorAtTheAppointmentTimeWithExpertise(mondayAt10, Expertise.CARDIOLOGIA).orElse(null);

        // Then
        assertThat(freeDoctor).isNull();
    }

    @Test
    @DisplayName("Deve retornar null quando medico1 estiver disponível mas inativo")
    void testFindRandomFreeDoctorAtTheAppointmentTimeWithExpertiseCase5() {
        // Given
        LocalDateTime mondayAt10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
        var doctor1 = createDoctor();
        doctor1.inactivate();
        em.persist(doctor1);

        // When
        Doctor freeDoctor = repository.findRandomFreeDoctorAtTheAppointmentTimeWithExpertise(mondayAt10, Expertise.CARDIOLOGIA).orElse(null);

        // Then
        assertThat(freeDoctor).isNull();
    }

    @Test
    @DisplayName("Deve retornar medico1 quando medico1 estiver ocupado, mas consulta estiver inativa")
    void testFindRandomFreeDoctorAtTheAppointmentTimeWithExpertiseCase6() {
        // Given
        LocalDateTime mondayAt10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
        var doctor1 = createDoctor();
        var patient = createPatient();
        var appointment = createAppointment(doctor1, patient, mondayAt10);
        appointment.cancel(ReasonForCancellation.OTHER);
        em.persist(appointment);

        // When
        Doctor freeDoctor = repository.findRandomFreeDoctorAtTheAppointmentTimeWithExpertise(mondayAt10, Expertise.CARDIOLOGIA).orElse(null);

        // Then
        assertThat(freeDoctor).isEqualTo(doctor1);
    }

    private AddressDTO createAddressDTO() {
        return new AddressDTO("Rua Teste", "Bairro Teste", "12345678", "Cidade Teste",
                "XX", "123", "Complemento 1");
    }

    private Doctor createDoctor() {
        return createDoctor("medico1", Expertise.CARDIOLOGIA);
    }

    private Doctor createDoctor(String name, Expertise expertise) {
        String crm = String.valueOf(new Random().nextInt(100000, 999999));

        var doctor = new Doctor(
                new DoctorCreateDTO(name, name + "@mail.com", crm, "1234-5678",
                        expertise, createAddressDTO())
        );
        em.persist(doctor);
        return doctor;
    }

    private Patient createPatient() {
        var patient = new Patient(
                new NewPatientDTO("Paciente 1", "paciente1@mail.com", "1234-5678",
                        "00000000000", createAddressDTO())
        );
        em.persist(patient);
        return patient;
    }

    private Appointment createAppointment(Doctor doctor, Patient patient, LocalDateTime appointmentTime) {
        var appointment = new Appointment(patient, doctor, appointmentTime);
        em.persist(appointment);
        return appointment;
    }

}