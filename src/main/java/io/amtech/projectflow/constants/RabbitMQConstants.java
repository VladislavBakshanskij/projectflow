package io.amtech.projectflow.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class RabbitMQConstants {
    public static final String NOTIFICATION_QUEUE = "notification";
    public static final String NOTIFICATION_PROJECT_EXCHANGE = "project";
    public static final String NOTIFY_PROJECT_ROUTING_KEY = "notify.project";
}