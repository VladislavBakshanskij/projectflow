package io.amtech.projectflow.service.direction;

import io.amtech.projectflow.dto.request.direction.DirectionCreateDto;
import io.amtech.projectflow.dto.request.direction.DirectionUpdateDto;
import io.amtech.projectflow.dto.response.direction.DirectionDto;
import io.amtech.projectflow.model.Direction;
import io.amtech.projectflow.repository.direction.DirectionRepository;
import io.amtech.projectflow.model.DirectionWithLeadName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DirectionServiceImpl implements DirectionService {
    private final DirectionRepository directionRepository;

    @Override
    public DirectionDto create(final DirectionCreateDto dto) {
        final Direction directionToSave = new Direction()
                .setName(dto.getName())
                .setLeadId(dto.getLeadId());
        final DirectionWithLeadName direction = directionRepository.save(directionToSave);
        return new DirectionDto()
                .setId(direction.getId())
                .setName(direction.getName())
                .setLeadId(direction.getLeadId())
                .setLeadName(direction.getLeadName());
    }

    @Override
    public DirectionDto get(final UUID id) {
        final DirectionWithLeadName direction = directionRepository.findById(id);
        return new DirectionDto()
                .setId(direction.getId())
                .setName(direction.getName())
                .setLeadId(direction.getLeadId())
                .setLeadName(direction.getLeadName());
    }

    @Override
    public void update(final UUID id, final DirectionUpdateDto dto) {
        final Direction direction = new Direction()
                .setName(dto.getName())
                .setLeadId(dto.getLeadId());
        directionRepository.update(id, direction);
    }

    @Override
    public void delete(final UUID id) {
        directionRepository.delete(id);
    }
}
