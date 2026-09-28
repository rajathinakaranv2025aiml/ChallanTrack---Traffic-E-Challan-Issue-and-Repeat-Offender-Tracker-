package com.example.ChallanTrack.service;

import com.example.ChallanTrack.entity.ViolationType;
import com.example.ChallanTrack.exception.BusinessRuleException;
import com.example.ChallanTrack.exception.ResourceNotFoundException;
import com.example.ChallanTrack.repository.ViolationTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ViolationTypeService {

    private final ViolationTypeRepository repository;

    public ViolationTypeService(
            ViolationTypeRepository repository) {

        this.repository = repository;
    }

    @Transactional
    public ViolationType createViolation(
            ViolationType violationType) {

        String name =
                violationType.getViolationName()
                        .trim();

        if (repository
                .findByViolationNameIgnoreCase(name)
                .isPresent()) {

            throw new BusinessRuleException(
                    "Violation type already exists: "
                            + name
            );
        }

        violationType.setViolationName(name);

        return repository.save(violationType);
    }

    public List<ViolationType> getAllViolations() {
        return repository.findAll();
    }

    public ViolationType getViolation(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Violation type not found with ID: "
                                        + id
                        )
                );
    }
}
