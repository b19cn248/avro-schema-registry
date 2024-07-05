package org.example.avroschemaregistry.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.avroschemaregistry.kafka.producer.KafkaMessageHelper;
import org.example.avroschemaregistry.kafka.producer.service.KafkaProducer;
import org.example.avroschemaregistry.model.Employee;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeePublisher {

  private final KafkaMessageHelper kafkaMessageHelper;
  private final KafkaProducer<String, Employee> kafkaProducer;

  public void publish(Employee employee) {

    String UID = UUID.randomUUID().toString();

    try {
      kafkaProducer.send("test",
            UID,
            employee,
            kafkaMessageHelper.getKafkaCallback(
                  "test",
                  employee,
                  UID,
                  "RestaurantApprovalResponseAvroModel"
            )
      );

      log.info("Employee send with ID: {}", UID);
    } catch (Exception e) {
      log.error("Error while sending Employee with ID: {}, error : {}", UID, e.getMessage());
    }
  }
}
