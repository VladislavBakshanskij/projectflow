package io.amtech.projectflow.service.employee;

import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.dto.request.employee.EmployeeCreateDto;
import io.amtech.projectflow.dto.request.employee.EmployeeUpdateDto;
import io.amtech.projectflow.dto.response.employee.EmployeeDto;

import java.util.UUID;

public interface EmployeeService {
    EmployeeDto get(UUID id);

    EmployeeDto create(EmployeeCreateDto dto);

    void update(UUID id, EmployeeUpdateDto dto);

    void delete(UUID id);

    PagedData<EmployeeDto> search(SearchCriteria searchCriteria);
}
