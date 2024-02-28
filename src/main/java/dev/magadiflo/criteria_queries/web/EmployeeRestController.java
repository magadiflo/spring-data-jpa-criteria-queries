package dev.magadiflo.criteria_queries.web;

import dev.magadiflo.criteria_queries.persistence.dao.IEmployeeSearchDao;
import dev.magadiflo.criteria_queries.persistence.entity.Employee;
import dev.magadiflo.criteria_queries.persistence.repository.IEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/employees")
public class EmployeeRestController {

    private final IEmployeeRepository employeeRepository;
    private final IEmployeeSearchDao employeeSearchDao;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(this.employeeRepository.findAll());
    }

    @GetMapping(path = "/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long employeeId) {
        return this.employeeRepository.findById(employeeId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/search-simple")
    public ResponseEntity<List<Employee>> getAllEmployeesSearchSimple(@RequestParam String firstName,
                                                                      @RequestParam String lastName,
                                                                      @RequestParam String email) {
        return ResponseEntity.ok(this.employeeSearchDao.findAllBySimpleQuery(firstName, lastName, email));
    }
}
