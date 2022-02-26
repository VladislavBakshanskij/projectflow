package io.amtech.projectflow.listener.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalEvent<T> {
    private JournalEventType type;
    private T data;
}
