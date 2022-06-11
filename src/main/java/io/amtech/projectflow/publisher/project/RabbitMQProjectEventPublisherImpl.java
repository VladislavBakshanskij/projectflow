package io.amtech.projectflow.publisher.project;

import io.amtech.projectflow.dto.request.project.ProjectJournalEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static io.amtech.projectflow.constants.RabbitMQConstants.NOTIFICATION_PROJECT_EXCHANGE;
import static io.amtech.projectflow.constants.RabbitMQConstants.NOTIFY_PROJECT_ROUTING_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQProjectEventPublisherImpl implements ProjectEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void notifyOnChanges(final ProjectJournalEvent data) {
        if (log.isDebugEnabled()) {
            log.debug("Send notification for project changes :: {}", data);
        }
        rabbitTemplate.convertAndSend(NOTIFICATION_PROJECT_EXCHANGE, NOTIFY_PROJECT_ROUTING_KEY, data,
                                      message -> {
                                          if (log.isDebugEnabled()) {
                                              log.debug("Sending project changes({}) to exchange({}) with routing_key({})",
                                                        data, NOTIFICATION_PROJECT_EXCHANGE, NOTIFY_PROJECT_ROUTING_KEY);
                                          }
                                          return MessageBuilder.fromMessage(message).build();
                                      });
    }
}
