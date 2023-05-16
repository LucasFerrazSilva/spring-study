package br.com.ferraz.springstudy.address;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor @AllArgsConstructor @Getter @ToString
public class Address {
    private String street;
    private String neighborhood;
    private String zipCode;
    private String city;
    private String state;
    private String number;
    private String complement;

    public Address(AddressDTO dto) {
        this.street = dto.street();
        this.neighborhood = dto.neighborhood();
        this.zipCode = dto.zipCode();
        this.city = dto.city();
        this.state = dto.state();
        this.number = dto.number();
        this.complement = dto.complement();
    }

    public void update(AddressDTO dto) {
        if (dto.street() != null)
            this.street = dto.street();

        if (dto.neighborhood() != null)
            this.neighborhood = dto.neighborhood();

        if (dto.zipCode() != null)
            this.zipCode = dto.zipCode();

        if (dto.city() != null)
            this.city = dto.city();

        if (dto.state() != null)
            this.state = dto.state();

        if (dto.number() != null)
            this.number = dto.number();

        if (dto.complement() != null)
            this.complement = dto.complement();
    }
}
