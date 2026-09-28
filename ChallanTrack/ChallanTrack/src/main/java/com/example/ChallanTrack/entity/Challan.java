package com.example.ChallanTrack.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "challans",
        indexes = {

                @Index(
                        name = "idx_vehicle_date",
                        columnList = "vehicle_id, issued_date"
                ),

                @Index(
                        name = "idx_vehicle_violation_date",
                        columnList = "vehicle_id, violation_type_id, issued_date"
                ),

                @Index(
                        name = "idx_status_date",
                        columnList = "status, issued_date"
                )
        }
)
public class Challan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long challanId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "violation_type_id", nullable = false)
    private ViolationType violationType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fineAmount;

    @Column(nullable = false)
    private LocalDateTime issuedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ChallanStatus status;

    public enum ChallanStatus {
        UNPAID,
        PAID
    }

    public Long getChallanId() {
        return challanId;
    }

    public void setChallanId(Long challanId) {
        this.challanId = challanId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public ViolationType getViolationType() {
        return violationType;
    }

    public void setViolationType(ViolationType violationType) {
        this.violationType = violationType;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }

    public ChallanStatus getStatus() {
        return status;
    }

    public void setStatus(ChallanStatus status) {
        this.status = status;
    }
}
