package io.amtech.projectflow.repository.employee;

import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.model.employee.Employee;

import java.util.UUID;

public interface EmployeeRepository {
    Employee findById(UUID id);

    Employee save(Employee e);

    void update(UUID id, Employee e);

    void delete(UUID id);

    PagedData<Employee> search(SearchCriteria searchCriteria);

    void checkOnExists(UUID id);
}
