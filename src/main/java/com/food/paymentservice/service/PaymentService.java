package com.food.paymentservice.service;

import com.food.paymentservice.client.OrderClient;
import com.food.paymentservice.dto.PaymentDto;
import com.food.paymentservice.enums.Status;
import com.food.paymentservice.model.Payment;
import com.food.paymentservice.repository.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderClient order;

    /* ModelMapper utiliza a classe de response para montar o Objeto de resposta,
     *  sem ter que dar set e get */

    public Page<PaymentDto> findAll(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(p -> modelMapper.map(p, PaymentDto.class));
    }

    public PaymentDto findById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(payment, PaymentDto.class);
    }

    public PaymentDto createPayment(PaymentDto response) {
        Payment payment = modelMapper.map(response, Payment.class);
        payment.setStatus(Status.CREATED);
        paymentRepository.save(payment);

        return modelMapper.map(payment, PaymentDto.class);
    }

    public PaymentDto updatePayment(Long id, PaymentDto response) {
        Payment payment = modelMapper.map(response, Payment.class);
        payment.setId(id);
        paymentRepository.save(payment);

        return modelMapper.map(payment, PaymentDto.class);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    public void aprovePayment(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);

        if(!payment.isPresent()) {
            throw new EntityNotFoundException();
        }

        payment.get().setStatus(Status.CONFIRMED);
        paymentRepository.save(payment.get());
        order.updatePayment(payment.get().getOrderId());
    }

    public void updateStatus(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);

        if(!payment.isPresent()) {
            throw new EntityNotFoundException();
        }

        payment.get().setStatus(Status.CONFIRMED_PENDING);
        paymentRepository.save(payment.get());
    }
}