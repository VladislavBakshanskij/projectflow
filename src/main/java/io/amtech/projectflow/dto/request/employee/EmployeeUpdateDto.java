package io.amtech.projectflow.dto.request.employee;

import io.amtech.projectflow.model.UserPosition;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EmployeeUpdateDto {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String phone;
    @NotNull
    private UserPosition position;
}
