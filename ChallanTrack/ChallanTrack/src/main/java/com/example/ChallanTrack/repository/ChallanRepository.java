package com.example.ChallanTrack.repository;

import com.example.ChallanTrack.entity.Challan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChallanRepository extends JpaRepository<Challan, Long> {

    // Find previous challans for the same vehicle and same violation
    List<Challan> findByVehicleVehicleIdAndViolationTypeViolationTypeIdAndIssuedDateAfter(
            Long vehicleId,
            Long violationTypeId,
            LocalDateTime date
    );

    // Find all challans of a vehicle within a time period
    List<Challan> findByVehicleVehicleIdAndIssuedDateAfter(
            Long vehicleId,
            LocalDateTime date
    );

    // Find unpaid challans older than a specific date
    List<Challan> findByStatusAndIssuedDateBefore(
            Challan.ChallanStatus status,
            LocalDateTime date
    );
}
