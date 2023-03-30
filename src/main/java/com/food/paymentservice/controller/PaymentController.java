package com.food.paymentservice.controller;

import com.food.paymentservice.dto.PaymentDto;
import com.food.paymentservice.service.PaymentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public Page<PaymentDto> list(@PageableDefault(size = 10) Pageable pageable) {
        return paymentService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> detail(@PathVariable @NotNull Long id) {
        PaymentDto response = paymentService.findById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PaymentDto> register(@RequestBody @Valid PaymentDto request,
                                                    UriComponentsBuilder uriBuilder) {

        PaymentDto response = paymentService.createPayment(request);
        URI path = uriBuilder.path("/payment/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(path).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> update(@PathVariable @NotNull Long id, @RequestBody @Valid PaymentDto request) {
        PaymentDto update = paymentService.updatePayment(id, request);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentDto> delete(@PathVariable @NotNull Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirm")
    @CircuitBreaker(name = "updateOrder", fallbackMethod = "paymentAuthorizedWithPending")
    public void confirmPayment(@PathVariable @NotNull Long id) {
        paymentService.aprovePayment(id);
    }

    public void paymentAuthorizedWithPending(Long id, Exception e) {
        paymentService.updateStatus(id);
    }
}