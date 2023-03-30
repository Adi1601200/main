package com.project.learn.main.kakfa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.learn.main.dto.Example;
import com.project.learn.main.entity.Post;
import com.project.learn.main.entity.Thumbnail;
import com.project.learn.main.repository.ThumbnailRepo;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @Autowired
    ThumbnailRepo thumbnailRepo;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "post", groupId = "myGroup")
    public void consumer(ConsumerRecord<?,String> record) throws JsonProcessingException {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Post post = objectMapper.readValue(record.value(), Post.class);
            Thumbnail thumbnail = new Thumbnail();
            thumbnail.setData(post.toString());
            thumbnailRepo.save(thumbnail);
        }catch (Exception e){
            System.out.println("exception occurred" + e.getMessage());
        }



    }
}
