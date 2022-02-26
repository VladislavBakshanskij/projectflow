package io.amtech.projectflow.model.auth;

import io.amtech.projectflow.model.employee.Employee;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserWithEmployee implements Serializable {
    private User user;
    private Employee employee;
}
