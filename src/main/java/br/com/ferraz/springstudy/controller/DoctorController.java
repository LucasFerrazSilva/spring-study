package br.com.ferraz.springstudy.controller;

import br.com.ferraz.springstudy.doctor.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicos")
public class DoctorController {

    private final DoctorRepository repository;

    public DoctorController(DoctorRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public String add(@RequestBody @Valid DoctorCreateDTO doctorCreateDTO) {
        Doctor doctor = new Doctor(doctorCreateDTO);
        repository.save(doctor);
        return String.format("Médico %s adicionado com sucesso!", doctor.getName());
    }

    @GetMapping
    public Page<DoctorReadDTO> list(@PageableDefault(sort="name", direction = Sort.Direction.ASC, size=10) Pageable pageable) {
        Page<Doctor> doctors = repository.findAll(pageable);
        Page<DoctorReadDTO> list = doctors.map(DoctorReadDTO::new);
        return list;
    }

    @PutMapping
    @Transactional
    public String update(@RequestBody @Valid DoctorUpdateDTO doctorDTO) {
        Doctor doctor = repository.getReferenceById(doctorDTO.id());
        doctor.update(doctorDTO);
        return String.format("Médico %s atualizado.", doctor.getName());
    }

}
