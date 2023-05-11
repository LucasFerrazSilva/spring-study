package br.com.ferraz.springstudy.controller;

import br.com.ferraz.springstudy.model.Doctor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/medicos")
public class DoctorController {

    public static List<Doctor> doctors = new ArrayList<>();

    @PostMapping
    public String add(@RequestBody Doctor doctor) {
        doctors.add(doctor);
        return String.format("Médico %s adicionado com sucesso!", doctor.getName());
    }

    @GetMapping
    public String list() {
        String doctorsNames = String.join(", ", doctors.stream().map(doctor -> doctor.getName()).toList());

        return String.format("Médicos: %s", doctorsNames);
    }

}
