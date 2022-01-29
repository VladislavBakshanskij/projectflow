package io.amtech.projectflow.service.employee;

import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.dto.request.employee.EmployeeCreateDto;
import io.amtech.projectflow.dto.request.employee.EmployeeUpdateDto;
import io.amtech.projectflow.dto.response.employee.EmployeeDto;
import io.amtech.projectflow.model.Employee;
import io.amtech.projectflow.repository.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto get(final UUID id) {
        final Employee employee = employeeRepository.findById(id);
        return new EmployeeDto()
                .setId(employee.getId())
                .setName(employee.getName())
                .setEmail(employee.getEmail())
                .setFired(employee.isFired())
                .setPhone(employee.getPhone())
                .setPosition(employee.getUserPosition());
    }

    @Override
    public EmployeeDto create(final EmployeeCreateDto dto) {
        final Employee employeeToSave = new Employee()
                .setName(dto.getName())
                .setEmail(dto.getEmail())
                .setPhone(dto.getPhone())
                .setUserPosition(dto.getUserPosition());
        final Employee employee = employeeRepository.save(employeeToSave);
        return new EmployeeDto()
                .setId(employee.getId())
                .setName(dto.getName())
                .setEmail(employee.getEmail())
                .setFired(employee.isFired())
                .setPhone(employee.getPhone())
                .setPosition(employee.getUserPosition());
    }

    @Override
    public void update(final UUID id, final EmployeeUpdateDto dto) {
        final Employee employee = new Employee()
                .setName(dto.getName())
                .setEmail(dto.getEmail())
                .setPhone(dto.getPhone())
                .setUserPosition(dto.getPosition());
        employeeRepository.update(id, employee);
    }

    @Override
    public void delete(final UUID id) {
        employeeRepository.delete(id);
    }

    @Override
    public PagedData<EmployeeDto> search(final SearchCriteria searchCriteria) {
        return employeeRepository.search(searchCriteria)
                .map(employee -> new EmployeeDto()
                        .setId(employee.getId())
                        .setName(employee.getName())
                        .setEmail(employee.getEmail())
                        .setFired(employee.isFired())
                        .setPhone(employee.getPhone())
                        .setPosition(employee.getUserPosition()));
    }
}
