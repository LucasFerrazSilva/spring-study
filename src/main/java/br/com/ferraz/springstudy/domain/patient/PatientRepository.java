package br.com.ferraz.springstudy.domain.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Page<Patient> findByActiveIsTrue(Pageable pageable);
    Optional<Patient> findByIdAndActiveIsTrue(Long id);

}
