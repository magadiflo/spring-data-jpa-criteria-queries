package dev.magadiflo.criteria_queries.persistence.repository;

import dev.magadiflo.criteria_queries.persistence.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDepartmentRepository extends JpaRepository<Department, Long> {
}
