package br.com.ferraz.springstudy.model;

import lombok.Data;

@Data
public class Doctor {

    private String name;
    private String email;
    private Integer crm;
    private String expertise;
    private Address address;

}
