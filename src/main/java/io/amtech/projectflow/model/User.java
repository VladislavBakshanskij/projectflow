package io.amtech.projectflow.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class User implements Serializable {
    private UUID employeeId;
    private String login;
    private String password;
    private boolean isLocked;
}
