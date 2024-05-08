package com.green.orderservice.producer;

import com.green.orderservice.model.Payment;
import com.green.orderservice.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class PaymentProducer {

    @Autowired
    private KafkaTemplate<String, Payment> kafkaTemplate;

    public void pushToPayment(Payment payment) {
        var LOG_NAME = "pushToPayment";
        log.info("{} Begin...", LOG_NAME);
        log.info("{} payment {}", LOG_NAME, payment);

        CompletableFuture<SendResult<String, Payment>> completableFuture = kafkaTemplate.sendDefault(Constant.Payment.TOPIC_CREATE_PAYMENT, payment);
        completableFuture.whenCompleteAsync((success, error) -> {
            if (ObjectUtils.isEmpty(error)) {
                log.error("{} Payment send failed {}", LOG_NAME, error.getMessage());
            } else {
                log.info("{} Payment send Successfully to Topic {}, Message {} ", LOG_NAME, success.getProducerRecord().topic(), success.getProducerRecord().value());
            }
        });

        log.info("{} End.", LOG_NAME);
    }
}
