package dev.magadiflo.criteria_queries.web;

import dev.magadiflo.criteria_queries.persistence.entity.Department;
import dev.magadiflo.criteria_queries.persistence.repository.IDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/departments")
public class DepartmentRestController {

    private final IDepartmentRepository departmentRepository;

    @GetMapping
    public ResponseEntity<List<Department>> getAllEmployees() {
        return ResponseEntity.ok(this.departmentRepository.findAll());
    }

    @GetMapping(path = "/{departmentId}")
    public ResponseEntity<Department> getEmployee(@PathVariable Long departmentId) {
        return this.departmentRepository.findById(departmentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
