package br.com.ferraz.springstudy.controller;

import br.com.ferraz.springstudy.domain.patient.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pacientes")
public class PatientController {

    private PatientRepository repository;

    public PatientController(PatientRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity save(@RequestBody @Valid NewPatientDTO dto, UriComponentsBuilder uriBuilder) {
        Patient patient = new Patient(dto);
        repository.save(patient);

        URI uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(patient.getId()).toUri();
        PatientInfosDTO infosDTO = new PatientInfosDTO(patient);

        return ResponseEntity.created(uri).body(infosDTO);
    }

    @PutMapping
    @Transactional
    public ResponseEntity update(@RequestBody @Valid UpdatePatientDTO dto) {
        Patient patient = repository.getReferenceById(dto.id());
        patient.update(dto);
        PatientInfosDTO infosDTO = new PatientInfosDTO(patient);
        return ResponseEntity.ok(infosDTO);
    }

    @GetMapping
    public ResponseEntity list(@PageableDefault Pageable pageable) {
        Page<Patient> patients = repository.findByActiveIsTrue(pageable);
        Page<PatientListDTO> patientListDTOS = patients.map(PatientListDTO::new);
        return ResponseEntity.ok(patientListDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        Patient patient = repository.getReferenceById(id);
        PatientInfosDTO patientDTO = new PatientInfosDTO(patient);

        return ResponseEntity.ok(patientDTO);
    }

    // Delete

}
