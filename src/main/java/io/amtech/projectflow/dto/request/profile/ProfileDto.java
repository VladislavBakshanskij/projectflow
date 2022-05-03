package io.amtech.projectflow.dto.request.profile;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class ProfileDto {
    private User user;
    private Employee employee;

    @Data
    @Accessors(chain = true)
    public static class User {
        private UUID id;
        private String username;
    }

    @Data
    @Accessors(chain = true)
    public static class Employee {
        private UUID id;
        private String position;
    }
}
