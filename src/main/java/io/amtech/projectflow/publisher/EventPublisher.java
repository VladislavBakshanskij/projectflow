package io.amtech.projectflow.publisher;

public interface EventPublisher<T> {
    void notifyOnChanges(T data);
}
