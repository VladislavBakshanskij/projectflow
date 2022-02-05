package io.amtech.projectflow.service.direction;

import io.amtech.projectflow.dto.request.direction.DirectionCreateDto;
import io.amtech.projectflow.dto.request.direction.DirectionUpdateDto;
import io.amtech.projectflow.dto.response.direction.DirectionDto;

import java.util.UUID;

public interface DirectionService {
    DirectionDto create(DirectionCreateDto dto);

    DirectionDto get(UUID id);

    void update(UUID id, DirectionUpdateDto dto);

    void delete(UUID id);
}
