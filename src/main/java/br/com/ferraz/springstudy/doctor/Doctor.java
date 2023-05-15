package br.com.ferraz.springstudy.doctor;

import br.com.ferraz.springstudy.address.Address;
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
    @Enumerated(EnumType.STRING)
    private Expertise expertise;
    @Embedded
    private Address address;

    public Doctor(DoctorDTO dto) {
        this.name = dto.name();
        this.email = dto.email();
        this.crm = dto.crm();
        this.expertise = dto.expertise();
        this.address = new Address(dto.address());
    }
}
