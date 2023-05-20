package br.com.ferraz.springstudy.controller;

import br.com.ferraz.springstudy.domain.patient.NewPatientDTO;
import br.com.ferraz.springstudy.domain.patient.Patient;
import br.com.ferraz.springstudy.domain.patient.PatientInfosDTO;
import br.com.ferraz.springstudy.domain.patient.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pacientes")
public class PatientController {

    private PatientRepository repository;

    public PatientController(PatientRepository repository) {
        this.repository = repository;
    }


    // Create
    @PostMapping
    public ResponseEntity save(@RequestBody @Valid NewPatientDTO dto, UriComponentsBuilder uriBuilder) {
        Patient patient = new Patient(dto);
        repository.save(patient);

        URI uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(patient.getId()).toUri();
        PatientInfosDTO infosDTO = new PatientInfosDTO(patient);

        return ResponseEntity.created(uri).body(infosDTO);
    }

    // Update
    // Read (list, get)
    // Delete

}
