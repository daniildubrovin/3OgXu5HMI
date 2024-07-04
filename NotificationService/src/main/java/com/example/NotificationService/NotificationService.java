package com.example.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @KafkaListener(topics = "notifications", groupId="example")
    void listen_kafka(String msg) throws JsonProcessingException{
        Notification notification = new ObjectMapper().readValue(msg, Notification.class);
        notificationRepository.save(notification);

        log.info(notification.toString());
    }
}
