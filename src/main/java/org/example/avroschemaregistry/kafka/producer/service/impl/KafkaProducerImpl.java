package org.example.avroschemaregistry.kafka.producer.service.impl;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.example.avroschemaregistry.kafka.producer.exception.KafkaProducerException;
import org.example.avroschemaregistry.kafka.producer.service.KafkaProducer;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {

  private final KafkaTemplate<K, V> kafkaTemplate;

  public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void send(String topicName, K key, V message, CompletableFuture<SendResult<K, V>> callback) {
    log.info("Sending message={} to topic={}", message, topicName);
    try {
      CompletableFuture<SendResult<K, V>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
      kafkaResultFuture.whenComplete((result, ex) -> {
        if (ex != null) {
          callback.completeExceptionally(ex);
        } else {
          callback.complete(result);
        }
      });
    } catch (KafkaException e) {
      log.error("Error on kafka producer with key: {}, message: {} and exception: {}", key, message,
            e.getMessage());
      throw new KafkaProducerException("Error on kafka producer with key: " + key + " and message: " + message);
    }
  }

  @PreDestroy
  public void close() {
    if (kafkaTemplate != null) {
      log.info("Closing kafka producer!");
      kafkaTemplate.destroy();
    }
  }
}
