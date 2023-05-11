package br.com.ferraz.springstudy.doctor;

import br.com.ferraz.springstudy.address.Address;
import lombok.Data;

@Data
public class Doctor {

    private String name;
    private String email;
    private Integer crm;
    private Expertise expertise;
    private Address address;

}
