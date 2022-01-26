package io.amtech.projectflow.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Notification {
    private UUID id;
    private String recepient;
    private String sender;
    private String subject;
    private String body;
    private Instant createDate;
}
