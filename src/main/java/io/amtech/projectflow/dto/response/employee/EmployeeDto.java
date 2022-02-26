package io.amtech.projectflow.dto.response.employee;

import io.amtech.projectflow.model.employee.UserPosition;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class EmployeeDto {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private UserPosition position;
    private boolean isFired = false;
}
