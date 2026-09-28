package com.example.ChallanTrack.repository;

import com.example.ChallanTrack.entity.ViolationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViolationTypeRepository extends JpaRepository<ViolationType, Long> {

    Optional<ViolationType> findByViolationNameIgnoreCase(String violationName);
}
