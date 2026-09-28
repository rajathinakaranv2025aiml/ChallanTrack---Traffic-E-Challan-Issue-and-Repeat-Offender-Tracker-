package com.example.ChallanTrack.service;

import com.example.ChallanTrack.dto.ChallanRequest;
import com.example.ChallanTrack.entity.Challan;
import com.example.ChallanTrack.entity.Vehicle;
import com.example.ChallanTrack.entity.ViolationType;
import com.example.ChallanTrack.exception.ResourceNotFoundException;
import com.example.ChallanTrack.repository.ChallanRepository;
import com.example.ChallanTrack.repository.VehicleRepository;
import com.example.ChallanTrack.repository.ViolationTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChallanService {

    private final ChallanRepository challanRepository;

    private final VehicleRepository vehicleRepository;

    private final ViolationTypeRepository violationTypeRepository;

    public ChallanService(
            ChallanRepository challanRepository,
            VehicleRepository vehicleRepository,
            ViolationTypeRepository violationTypeRepository) {

        this.challanRepository = challanRepository;
        this.vehicleRepository = vehicleRepository;
        this.violationTypeRepository = violationTypeRepository;
    }

    @Transactional
    public Challan issueChallan(
            ChallanRequest request) {

        // Find vehicle
        Vehicle vehicle =
                vehicleRepository.findById(
                        request.getVehicleId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle not found with ID: "
                                        + request.getVehicleId()
                        )
                );

        // Find violation
        ViolationType violationType =
                violationTypeRepository.findById(
                        request.getViolationTypeId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Violation type not found with ID: "
                                        + request.getViolationTypeId()
                        )
                );

        LocalDateTime now =
                LocalDateTime.now();

        // Rolling 12-month window
        LocalDateTime windowStart =
                now.minusMonths(12);

        // Count previous same violation
        List<Challan> previousViolations =
                challanRepository
                        .findByVehicleVehicleIdAndViolationTypeViolationTypeIdAndIssuedDateAfter(
                                vehicle.getVehicleId(),
                                violationType.getViolationTypeId(),
                                windowStart
                        );

        int previousCount =
                previousViolations.size();

        // Calculate escalated fine
        BigDecimal fine =
                calculateFine(
                        violationType.getBaseFine(),
                        violationType.getEscalationPercent(),
                        previousCount
                );

        // Create challan
        Challan challan =
                new Challan();

        challan.setVehicle(vehicle);

        challan.setViolationType(
                violationType
        );

        challan.setFineAmount(fine);

        challan.setIssuedDate(now);

        challan.setStatus(
                Challan.ChallanStatus.UNPAID
        );

        return challanRepository.save(challan);
    }

    private BigDecimal calculateFine(
            BigDecimal baseFine,
            BigDecimal escalationPercent,
            int previousCount) {

        /*
         * Example:
         *
         * Base = 1000
         * Escalation = 50%
         *
         * First = 1000
         * Second = 1500
         * Third = 2250
         * Fourth = 3375
         */

        BigDecimal escalation =
                escalationPercent
                        .divide(
                                BigDecimal.valueOf(100),
                                10,
                                RoundingMode.HALF_UP
                        );

        BigDecimal multiplier =
                BigDecimal.ONE.add(escalation);

        BigDecimal finalMultiplier =
                multiplier.pow(previousCount);

        return baseFine
                .multiply(finalMultiplier)
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }

    public List<Challan> getAllChallans() {

        return challanRepository.findAll();
    }

    public Challan getChallanById(Long id) {

        return challanRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Challan not found with ID: "
                                        + id
                        )
                );
    }

    public List<Challan> getVehicleHistory(
            Long vehicleId) {

        if (!vehicleRepository.existsById(vehicleId)) {

            throw new ResourceNotFoundException(
                    "Vehicle not found with ID: "
                            + vehicleId
            );
        }

        LocalDateTime start =
                LocalDateTime.now()
                        .minusMonths(12);

        return challanRepository
                .findByVehicleVehicleIdAndIssuedDateAfter(
                        vehicleId,
                        start
                );
    }

    public List<Challan> getUnpaidOlderThan30Days() {

        LocalDateTime cutoff =
                LocalDateTime.now()
                        .minusDays(30);

        return challanRepository
                .findByStatusAndIssuedDateBefore(
                        Challan.ChallanStatus.UNPAID,
                        cutoff
                );
    }
}
