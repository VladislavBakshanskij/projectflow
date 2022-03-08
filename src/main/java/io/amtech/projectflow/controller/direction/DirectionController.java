package io.amtech.projectflow.controller.direction;

import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.app.SearchCriteriaBuilder;
import io.amtech.projectflow.dto.request.direction.DirectionCreateDto;
import io.amtech.projectflow.dto.request.direction.DirectionUpdateDto;
import io.amtech.projectflow.dto.response.direction.DirectionDto;
import io.amtech.projectflow.service.direction.DirectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("directions")
@RequiredArgsConstructor
public class DirectionController {
    private final DirectionService directionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DirectionDto create(@RequestBody @Valid DirectionCreateDto dto) {
        return directionService.create(dto);
    }

    @GetMapping("{id}")
    public DirectionDto get(@PathVariable UUID id) {
        return directionService.get(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") UUID id, @RequestBody @Valid DirectionUpdateDto dto) {
        directionService.update(id, dto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        directionService.delete(id);
    }

    @GetMapping
    public PagedData<DirectionDto> search(@RequestParam(required = false, defaultValue = "100") Integer limit,
                                          @RequestParam(required = false, defaultValue = "0") Integer offset,
                                          @RequestParam(required = false) String orders,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(required = false) UUID leadId,
                                          @RequestParam(required = false) String leadName) {
        final SearchCriteria criteria = new SearchCriteriaBuilder()
                .limit(limit)
                .offset(offset)
                .filter("name", name)
                .filter("leadId", Optional.ofNullable(leadId).map(Objects::toString).orElse(null))
                .filter("leadName", leadName)
                .order(orders)
                .build();
        return directionService.search(criteria);
    }
}
