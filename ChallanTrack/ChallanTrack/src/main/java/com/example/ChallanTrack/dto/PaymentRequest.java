package com.example.ChallanTrack.dto;

public class PaymentRequest {

    private String paymentMethod;

    public PaymentRequest() {
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}