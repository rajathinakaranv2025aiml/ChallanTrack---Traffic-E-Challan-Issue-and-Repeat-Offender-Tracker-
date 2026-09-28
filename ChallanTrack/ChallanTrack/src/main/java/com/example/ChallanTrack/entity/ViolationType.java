package com.example.ChallanTrack.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "violation_types")
public class ViolationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long violationTypeId;

    @Column(nullable = false, unique = true)
    private String violationName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal baseFine;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal escalationPercent;

    public ViolationType() {
    }

    public Long getViolationTypeId() {
        return violationTypeId;
    }

    public void setViolationTypeId(Long violationTypeId) {
        this.violationTypeId = violationTypeId;
    }

    public String getViolationName() {
        return violationName;
    }

    public void setViolationName(String violationName) {
        this.violationName = violationName;
    }

    public BigDecimal getBaseFine() {
        return baseFine;
    }

    public void setBaseFine(BigDecimal baseFine) {
        this.baseFine = baseFine;
    }

    public BigDecimal getEscalationPercent() {
        return escalationPercent;
    }

    public void setEscalationPercent(BigDecimal escalationPercent) {
        this.escalationPercent = escalationPercent;
    }
}