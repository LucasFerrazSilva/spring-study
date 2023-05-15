package br.com.ferraz.springstudy.controller;

import br.com.ferraz.springstudy.doctor.Doctor;
import br.com.ferraz.springstudy.doctor.DoctorDTO;
import br.com.ferraz.springstudy.doctor.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/medicos")
public class DoctorController {

    private final DoctorRepository repository;

    public DoctorController(DoctorRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public String add(@RequestBody DoctorDTO doctorDTO) {
        Doctor doctor = new Doctor(doctorDTO);
        repository.save(doctor);
        return String.format("Médico %s adicionado com sucesso!", doctor.getName());
    }

    @GetMapping
    public String list() {
        List<Doctor> doctors = repository.findAll();
        String doctorsNames = String.join(", ", doctors.stream().map(doctor -> doctor.toString()).toList());
        return String.format("Médicos: %s", doctorsNames);
    }

}
