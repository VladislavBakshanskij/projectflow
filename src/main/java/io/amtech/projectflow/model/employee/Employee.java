package io.amtech.projectflow.model.employee;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

@Accessors(chain = true)
@Data
public class Employee implements Serializable {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private UserPosition userPosition;
    private boolean isFired = false;
}
