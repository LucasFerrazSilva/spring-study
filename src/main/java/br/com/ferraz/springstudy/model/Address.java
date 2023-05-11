package br.com.ferraz.springstudy.model;

import lombok.Data;

@Data
public class Address {

    private String street;
    private String neighborhood;
    private String zipCode;
    private String city;
    private String state;
    private String number;
    private String complement;

}
