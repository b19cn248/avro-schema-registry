package org.example.avroschemaregistry.subcriber;

import lombok.extern.slf4j.Slf4j;
import org.example.avroschemaregistry.model.Employee;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmployeeSubscriber {

  @KafkaListener(topics = "test", groupId = "test-group")
  public void receive(
        @Payload List<Employee> employees,
        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
        @Header(KafkaHeaders.OFFSET) List<Long> offsets
  ) {
    log.info("{} number of employees received with keys {}, partitions {} and offsets {}",
          employees.size(),
          keys.toString(),
          partitions.toString(),
          offsets.toString());

    employees.forEach(employee -> {
      log.info("Processing employee with id : {}", employee.getId());
    });
  }
}
