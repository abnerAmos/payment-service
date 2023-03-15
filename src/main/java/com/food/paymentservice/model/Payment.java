package com.food.paymentservice.model;

import com.food.paymentservice.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull // Campo não pode ser nulo
    @Positive // Só aceita numeros positivos
    private BigDecimal amount;

    @NotBlank // Não pode ser num nem vazio
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 19)
    private String cardNumber;

    @NotBlank
    @Size(max = 7)
    private String expiration;

    @NotBlank
    @Size(min = 3, max = 3)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private Long orderId;

    @NotNull
    private Long typePaymentId;

}
