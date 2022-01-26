package io.amtech.projectflow.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Accessors(chain = true)
@Data
public class Employee {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private UserPosition userPosition;
    private boolean isFired;
}
