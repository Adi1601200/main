package com.project.learn.main.kakfa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private KafkaTemplate<String,Object> kakfaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kakfaTemplate) {
        this.kakfaTemplate = kakfaTemplate;
    }

    public void sendMessage(Object message){
        LOGGER.info(String.format("Message sent -> %s",message));
        kakfaTemplate.send("post",message);
    }
}
