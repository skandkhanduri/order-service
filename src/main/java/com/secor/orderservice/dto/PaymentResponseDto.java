package com.secor.orderservice.dto;


public class PaymentResponseDto {
    private String message;
    private String requestId;
    private String status;

    public PaymentResponseDto(String message, String requestId, String status) {
        this.message = message;
        this.requestId = requestId;
        this.status = status;
    }
    public PaymentResponseDto() {

    }

    public String getMessage() {
        return message;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
