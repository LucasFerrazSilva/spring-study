package br.com.ferraz.springstudy.address;

public record AddressDTO(
        String street,
        String neighborhood,
        String zipCode,
        String city,
        String state,
        String number,
        String complement
) {
}
