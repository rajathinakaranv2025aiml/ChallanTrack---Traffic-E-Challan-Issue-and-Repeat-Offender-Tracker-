package com.example.ChallanTrack.controller;

import com.example.ChallanTrack.dto.VehicleRequest;
import com.example.ChallanTrack.entity.Vehicle;
import com.example.ChallanTrack.service.VehicleService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // ==============================
    // CREATE VEHICLE
    // ==============================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicle createVehicle(
            @RequestBody VehicleRequest request) {

        return vehicleService.createVehicle(request);
    }

    // ==============================
    // GET ALL VEHICLES
    // ==============================

    @GetMapping
    public List<Vehicle> getAllVehicles() {

        return vehicleService.getAllVehicles();
    }

    // ==============================
    // GET VEHICLE BY ID
    // ==============================

    @GetMapping("/{id}")
    public Vehicle getVehicle(
            @PathVariable Long id) {

        return vehicleService.getVehicle(id);
    }
}