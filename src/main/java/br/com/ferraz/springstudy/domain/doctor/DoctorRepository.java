package br.com.ferraz.springstudy.domain.doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Page<Doctor> findAllByActiveIsTrue(Pageable pageable);

    Doctor findByIdAndActiveIsTrue(Long doctorId);

    Doctor findFirstByIdNotInAndActiveIsTrue(List<Long> occupiedDoctors);
}
