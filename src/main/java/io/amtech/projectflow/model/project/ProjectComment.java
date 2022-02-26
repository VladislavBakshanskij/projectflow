package io.amtech.projectflow.model.project;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ProjectComment {
    private UUID id;
    private UUID projectId;
    private String message;
    private Instant createDate;
    private String login;
}
