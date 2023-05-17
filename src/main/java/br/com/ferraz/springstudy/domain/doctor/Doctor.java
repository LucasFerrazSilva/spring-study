package br.com.ferraz.springstudy.domain.doctor;

import br.com.ferraz.springstudy.domain.address.Address;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter
@EqualsAndHashCode(of="id")
@ToString
@Entity
@Table(name="TB_DOCTORS")
public class Doctor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String crm;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Expertise expertise;
    @Embedded
    private Address address;
    @Column(nullable = false, columnDefinition = "TINYINT")
    private Boolean active;

    public Doctor(DoctorCreateDTO dto) {
        this.name = dto.name();
        this.email = dto.email();
        this.crm = dto.crm();
        this.phoneNumber = dto.phoneNumber();
        this.expertise = dto.expertise();
        this.address = new Address(dto.address());
        this.active = true;
    }

    public void update(DoctorUpdateDTO dto) {
        if (dto.name() != null)
            this.name = dto.name();

        if (dto.phoneNumber() != null)
            this.phoneNumber = dto.phoneNumber();

        if (dto.address() != null)
            this.address.update(dto.address());
    }

    public void inactivate() {
        this.active = false;
    }
}
