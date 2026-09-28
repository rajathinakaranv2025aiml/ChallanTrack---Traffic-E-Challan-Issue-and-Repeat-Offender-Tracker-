package com.example.ChallanTrack.controller;

import com.example.ChallanTrack.dto.ChallanRequest;
import com.example.ChallanTrack.entity.Challan;
import com.example.ChallanTrack.service.ChallanService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challans")
@CrossOrigin(origins = "*")
public class ChallanController {

    private final ChallanService challanService;

    public ChallanController(ChallanService challanService) {
        this.challanService = challanService;
    }

    // Issue new challan
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Challan issueChallan(
            @RequestBody ChallanRequest request) {

        return challanService.issueChallan(request);
    }

    // Get all challans
    @GetMapping
    public List<Challan> getAllChallans() {

        return challanService.getAllChallans();
    }

    // Get challan by ID
    @GetMapping("/{id}")
    public Challan getChallan(
            @PathVariable Long id) {

        return challanService.getChallanById(id);
    }

    // Get vehicle challan history
    @GetMapping("/vehicle/{vehicleId}/history")
    public List<Challan> getVehicleHistory(
            @PathVariable Long vehicleId) {

        return challanService.getVehicleHistory(vehicleId);
    }

    // Get unpaid challans older than 30 days
    @GetMapping("/unpaid/older-than-30-days")
    public List<Challan> getUnpaidOlderThan30Days() {

        return challanService.getUnpaidOlderThan30Days();
    }
}
