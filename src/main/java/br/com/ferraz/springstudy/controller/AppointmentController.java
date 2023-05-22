package br.com.ferraz.springstudy.controller;

import br.com.ferraz.springstudy.domain.appointment.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/consultas")
public class AppointmentController {

    private AppointmentRepository repository;
    private AppointmentService service;

    public AppointmentController(AppointmentRepository repository, AppointmentService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping
    @Transactional
    public ResponseEntity save(@RequestBody @Valid NewAppointmentDTO dto, UriComponentsBuilder uriBuilder) {
        Appointment appointment = service.scheduleAppointment(dto);

        URI uri = uriBuilder.path("/consultas/{id}").buildAndExpand(appointment.getId()).toUri();
        AppointmentInfosDTO infosDTO = new AppointmentInfosDTO(appointment);

        return ResponseEntity.created(uri).body(infosDTO);
    }
    @GetMapping
    public ResponseEntity<Page> list(@PageableDefault Pageable pageable) {
        Page<Appointment> list = repository.findByActiveIsTrue(pageable);
        Page<AppointmentListDTO> listDTOs = list.map(AppointmentListDTO::new);

        return ResponseEntity.ok(listDTOs);
    }

    // Read (Get)
    // Update
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity cancel(@RequestBody @Valid CancelAppointmentDTO dto) {
        service.cancelAppointment(dto);

        return ResponseEntity.noContent().build();
    }

}
