package dev.magadiflo.criteria_queries.persistence.repository;

import dev.magadiflo.criteria_queries.persistence.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
}
