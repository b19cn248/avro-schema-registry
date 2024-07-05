package org.example.avroschemaregistry.controller;

import org.example.avroschemaregistry.model.Employee;
import org.example.avroschemaregistry.publisher.EmployeePublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  private final EmployeePublisher employeePublisher;

  public TestController(EmployeePublisher employeePublisher) {
    this.employeePublisher = employeePublisher;
  }

  @PostMapping("/events")
  public String sendMessage(@RequestBody Employee employee) {
    employeePublisher.publish(employee);
    return "message published !";
  }
}
