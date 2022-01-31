package io.amtech.projectflow.controller.direction;

import io.amtech.projectflow.dto.request.direction.DirectionCreateDto;
import io.amtech.projectflow.dto.request.direction.DirectionUpdateDto;
import io.amtech.projectflow.dto.response.direction.DirectionDto;
import io.amtech.projectflow.service.direction.DirectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("directions")
@RequiredArgsConstructor
public class DirectionController {
    private final DirectionService directionService;

    @PostMapping
    public DirectionDto create(@RequestBody @Valid DirectionCreateDto dto) {
        return directionService.create(dto);
    }

    @GetMapping("{id}")
    public DirectionDto get(@PathVariable UUID id) {
        return directionService.get(id);
    }

    @PutMapping("{id}")
    public void update(@PathVariable("id") UUID id, @RequestBody @Valid DirectionUpdateDto dto) {
        directionService.update(id, dto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") UUID id) {
        directionService.delete(id);
    }
}
