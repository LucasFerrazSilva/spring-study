package br.com.ferraz.springstudy.domain.patient;

import br.com.ferraz.springstudy.domain.address.Address;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="TB_PATIENTS")
@Getter @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode(of="id")
public class Patient {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String cpf;
    @Embedded
    private Address address;
    @Column(columnDefinition = "TINYINT", nullable = false)
    private Boolean active;

    public Patient(NewPatientDTO dto) {
        this.name = dto.name();
        this.email = dto.email();
        this.phoneNumber = dto.email();
        this.cpf = dto.cpf();
        this.address = new Address(dto.address());
        this.active = true;
    }

    public void update(UpdatePatientDTO dto) {
        if (dto.name() != null)
            this.name = dto.name();
        if (dto.email() != null)
            this.email = dto.email();
        if (dto.phoneNumber() != null)
            this.phoneNumber = dto.phoneNumber();
        if (dto.cpf() != null)
            this.cpf = dto.cpf();
        if (dto.address() != null)
            this.address = new Address(dto.address());
    }
}
