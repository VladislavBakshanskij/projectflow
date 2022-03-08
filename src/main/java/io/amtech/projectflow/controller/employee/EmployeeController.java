package io.amtech.projectflow.controller.employee;

import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.app.SearchCriteriaBuilder;
import io.amtech.projectflow.dto.request.employee.EmployeeCreateDto;
import io.amtech.projectflow.dto.request.employee.EmployeeUpdateDto;
import io.amtech.projectflow.dto.response.employee.EmployeeDto;
import io.amtech.projectflow.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("{id}")
    public EmployeeDto get(@PathVariable("id") UUID id) {
        return employeeService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDto create(@Valid @RequestBody EmployeeCreateDto dto) {
        return employeeService.create(dto);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") UUID id, @Valid @RequestBody EmployeeUpdateDto dto) {
        employeeService.update(id, dto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        employeeService.delete(id);
    }

    @GetMapping
    public PagedData<EmployeeDto> search(@RequestParam(required = false, defaultValue = "100") Integer limit,
                                         @RequestParam(required = false, defaultValue = "0") Integer offset,
                                         @RequestParam(required = false, defaultValue = "name") String orders,
                                         @RequestParam(required = false) String name,
                                         @RequestParam(required = false) String email,
                                         @RequestParam(required = false) String phone,
                                         @RequestParam(required = false) String position,
                                         @RequestParam(required = false) Boolean fired) {
        final SearchCriteria criteria = new SearchCriteriaBuilder()
                .limit(limit)
                .offset(offset)
                .filter("name", name)
                .filter("email", email)
                .filter("phone", phone)
                .filter("position", position)
                .filter("fired", Optional.ofNullable(fired)
                        .map(Object::toString)
                        .orElse(Boolean.FALSE.toString()))
                .order(orders)
                .build();

        return employeeService.search(criteria);
    }
}
