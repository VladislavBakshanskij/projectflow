package io.amtech.projectflow.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Token implements Serializable {
    private String access;
    private String refresh;
    private UserWithEmployee user;
}
