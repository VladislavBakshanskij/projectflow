package io.amtech.projectflow.repository.direction;

import io.amtech.projectflow.model.Direction;
import io.amtech.projectflow.model.DirectionWithLeadName;

import java.util.UUID;

public interface DirectionRepository {
    DirectionWithLeadName save(Direction direction);

    DirectionWithLeadName findById(UUID id);

    void update(UUID id, Direction direction);

    void delete(UUID id);

    void checkOnExists(UUID id);
}
