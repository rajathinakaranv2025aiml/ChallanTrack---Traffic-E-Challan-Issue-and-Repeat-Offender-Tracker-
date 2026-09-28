package com.example.ChallanTrack.dto;

public class ChallanRequest {

    private Long vehicleId;

    private Long violationTypeId;

    public ChallanRequest() {
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getViolationTypeId() {
        return violationTypeId;
    }

    public void setViolationTypeId(Long violationTypeId) {
        this.violationTypeId = violationTypeId;
    }
}
