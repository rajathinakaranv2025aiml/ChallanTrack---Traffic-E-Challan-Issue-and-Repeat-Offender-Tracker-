package com.example.ChallanTrack.service;

import com.example.ChallanTrack.dto.VehicleRequest;
import com.example.ChallanTrack.entity.Vehicle;
import com.example.ChallanTrack.exception.BusinessRuleException;
import com.example.ChallanTrack.exception.ResourceNotFoundException;
import com.example.ChallanTrack.repository.VehicleRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle createVehicle(VehicleRequest request) {

        if (request.getVehicleNumber() == null ||
                request.getVehicleNumber().trim().isEmpty()) {

            throw new BusinessRuleException(
                    "Vehicle number is required"
            );
        }

        if (request.getOwnerName() == null ||
                request.getOwnerName().trim().isEmpty()) {

            throw new BusinessRuleException(
                    "Owner name is required"
            );
        }

        String vehicleNumber =
                request.getVehicleNumber()
                        .trim()
                        .toUpperCase();

        if (vehicleRepository
                .existsByVehicleNumberIgnoreCase(vehicleNumber)) {

            throw new BusinessRuleException(
                    "Vehicle already exists"
            );
        }

        Vehicle vehicle = new Vehicle();

        vehicle.setVehicleNumber(vehicleNumber);
        vehicle.setOwnerName(request.getOwnerName().trim());
        vehicle.setVehicleType(
                request.getVehicleType() == null
                        ? null
                        : request.getVehicleType().trim()
        );

        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {

        return vehicleRepository.findAll();
    }

    public Vehicle getVehicle(Long id) {

        return vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle not found with ID: " + id
                        )
                );
    }
}