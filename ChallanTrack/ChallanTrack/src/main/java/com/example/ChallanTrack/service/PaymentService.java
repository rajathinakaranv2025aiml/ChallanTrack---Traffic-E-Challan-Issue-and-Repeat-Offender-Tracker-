package com.example.ChallanTrack.service;

import com.example.ChallanTrack.dto.PaymentRequest;
import com.example.ChallanTrack.entity.Challan;
import com.example.ChallanTrack.entity.PaymentRecord;
import com.example.ChallanTrack.exception.BusinessRuleException;
import com.example.ChallanTrack.exception.ResourceNotFoundException;
import com.example.ChallanTrack.repository.ChallanRepository;
import com.example.ChallanTrack.repository.PaymentRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRecordRepository paymentRepository;
    private final ChallanRepository challanRepository;

    public PaymentService(
            PaymentRecordRepository paymentRepository,
            ChallanRepository challanRepository) {

        this.paymentRepository = paymentRepository;
        this.challanRepository = challanRepository;
    }

    public PaymentRecord makePayment(
            Long challanId,
            PaymentRequest request) {

        Challan challan = challanRepository.findById(challanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Challan not found with ID: " + challanId));

        if (challan.getStatus() == Challan.ChallanStatus.PAID) {
            throw new BusinessRuleException(
                    "This challan has already been paid");
        }

        if (paymentRepository.existsByChallanChallanId(challanId)) {
            throw new BusinessRuleException(
                    "Payment already exists for this challan");
        }

        if (request == null) {
            throw new BusinessRuleException(
                    "Payment request cannot be empty");
        }

        if (request.getPaymentMethod() == null) {
            throw new BusinessRuleException(
                    "Payment method is required");
        }

        if (request.getPaymentMethod().trim().isEmpty()) {
            throw new BusinessRuleException(
                    "Payment method is required");
        }

        PaymentRecord payment = new PaymentRecord();

        payment.setChallan(challan);
        payment.setPaymentAmount(challan.getFineAmount());
        payment.setPaymentDate(LocalDateTime.now());

        payment.setPaymentMethod(
                request.getPaymentMethod()
                        .trim()
                        .toUpperCase()
        );

        PaymentRecord savedPayment =
                paymentRepository.save(payment);

        challan.setStatus(Challan.ChallanStatus.PAID);

        challanRepository.save(challan);

        return savedPayment;
    }
}