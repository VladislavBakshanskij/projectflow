package io.amtech.projectflow.dto.response.user;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.management.relation.Relation;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class UserInfoResponse {
    private UUID id;
    private String role;
    private String name;
}
