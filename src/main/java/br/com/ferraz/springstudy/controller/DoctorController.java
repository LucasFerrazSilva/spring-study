package br.com.ferraz.springstudy.controller;

import br.com.ferraz.springstudy.doctor.DoctorDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/medicos")
public class DoctorController {

    public static List<DoctorDTO> doctors = new ArrayList<>();

    @PostMapping
    public String add(@RequestBody DoctorDTO doctor) {
        doctors.add(doctor);

        return String.format("Médico %s adicionado com sucesso!", doctor.name());
    }

    @GetMapping
    public String list() {
        String doctorsNames = String.join(", ", doctors.stream().map(doctor -> doctor.toString()).toList());

        return String.format("Médicos: %s", doctorsNames);
    }

}
