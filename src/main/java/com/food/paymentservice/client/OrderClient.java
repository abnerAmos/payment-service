package com.food.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("orders-ms")
public interface OrderClient {

    @PutMapping("/orders/{id}/paid-out")
    void updatePayment(@PathVariable Long id);

}
