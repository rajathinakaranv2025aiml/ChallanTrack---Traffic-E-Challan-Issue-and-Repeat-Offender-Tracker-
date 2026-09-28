package com.example.ChallanTrack.controller;

import com.example.ChallanTrack.dto.PaymentRequest;
import com.example.ChallanTrack.entity.PaymentRecord;
import com.example.ChallanTrack.service.PaymentService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ==============================
    // MAKE PAYMENT
    // ==============================

    @PostMapping("/{challanId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentRecord makePayment(
            @PathVariable Long challanId,
            @RequestBody PaymentRequest request) {

        return paymentService.makePayment(challanId, request);
    }
}
