package com.example.UberProject_BookingService.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "sample-topic")
    public void listen(String message){
        System.out.println("kafka message from this topic sample topic: " +message);
    }
}
