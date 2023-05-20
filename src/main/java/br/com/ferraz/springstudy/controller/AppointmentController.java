package br.com.ferraz.springstudy.controller;

import br.com.ferraz.springstudy.domain.appointment.Appointment;
import br.com.ferraz.springstudy.domain.appointment.AppointmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consultas")
public class AppointmentController {

    private AppointmentRepository repository;

    public AppointmentController(AppointmentRepository repository) {
        this.repository = repository;
    }

    // Create
    // Read (List, Get)
    @GetMapping
    public ResponseEntity<Page> list(@PageableDefault Pageable pageable) {
        Page<Appointment> list = repository.findAll(pageable);

        return ResponseEntity.ok(list);
    }

    // Update
    // Delete

}
