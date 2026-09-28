package com.example.ChallanTrack.controller;

import com.example.ChallanTrack.entity.ViolationType;
import com.example.ChallanTrack.service.ViolationTypeService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/violations")
@CrossOrigin(origins = "*")
public class ViolationTypeController {

    private final ViolationTypeService violationTypeService;

    public ViolationTypeController(ViolationTypeService violationTypeService) {
        this.violationTypeService = violationTypeService;
    }

    // ==============================
    // CREATE VIOLATION TYPE
    // ==============================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ViolationType createViolation(
            @RequestBody ViolationType violationType) {

        return violationTypeService.createViolation(violationType);
    }

    // ==============================
    // GET ALL VIOLATION TYPES
    // ==============================

    @GetMapping
    public List<ViolationType> getAllViolations() {

        return violationTypeService.getAllViolations();
    }

    // ==============================
    // GET VIOLATION TYPE BY ID
    // ==============================

    @GetMapping("/{id}")
    public ViolationType getViolation(
            @PathVariable Long id) {

        return violationTypeService.getViolation(id);
    }
}