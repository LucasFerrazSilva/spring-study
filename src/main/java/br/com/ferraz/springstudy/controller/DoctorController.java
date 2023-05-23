package br.com.ferraz.springstudy.controller;

import br.com.ferraz.springstudy.domain.doctor.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name="bearer-key")
public class DoctorController {

    private final DoctorRepository repository;

    public DoctorController(DoctorRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity add(@RequestBody @Valid DoctorCreateDTO doctorCreateDTO, UriComponentsBuilder uriBuilder) {
        Doctor doctor = new Doctor(doctorCreateDTO);
        repository.save(doctor);

        URI uri = uriBuilder.path("/medicos/{id}").buildAndExpand(doctor.getId()).toUri();
        DoctorInfosDTO dto = new DoctorInfosDTO(doctor);

        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    public ResponseEntity<Page<DoctorReadDTO>> list(@PageableDefault(sort="name", direction = Sort.Direction.ASC, size=10) Pageable pageable) {
        Page<Doctor> doctors = repository.findAllByActiveIsTrue(pageable);
        Page<DoctorReadDTO> list = doctors.map(DoctorReadDTO::new);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorInfosDTO> get(@PathVariable Long id) {
        Doctor doctor = repository.getReferenceById(id);
        DoctorInfosDTO doctorInfosDTO = new DoctorInfosDTO(doctor);
        return ResponseEntity.ok(doctorInfosDTO);
    }

    @PutMapping
    @Transactional
    public ResponseEntity update(@RequestBody @Valid DoctorUpdateDTO doctorDTO) {
        Doctor doctor = repository.getReferenceById(doctorDTO.id());
        doctor.update(doctorDTO);
        DoctorInfosDTO doctorInfosDTO = new DoctorInfosDTO(doctor);
        return ResponseEntity.ok(doctorInfosDTO);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable(name="id") Long id) {
        Doctor doctor = repository.getReferenceById(id);
        doctor.inactivate();
        return ResponseEntity.noContent().build();
    }

}
