package com.food.paymentservice.dto;

import com.food.paymentservice.enums.Status;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDto {

    private Long id;
    private BigDecimal amount;
    private String name;
    private String cardNumber;
    private String expiration;
    private String code;
    private Status status;
    private Long orderId;
    private Long typePaymentId;
}
