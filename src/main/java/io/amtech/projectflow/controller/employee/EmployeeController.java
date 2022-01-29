package io.amtech.projectflow.controller.employee;

import io.amtech.projectflow.dto.request.employee.EmployeeCreateDto;
import io.amtech.projectflow.dto.request.employee.EmployeeUpdateDto;
import io.amtech.projectflow.dto.response.employee.EmployeeDto;
import io.amtech.projectflow.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
}
