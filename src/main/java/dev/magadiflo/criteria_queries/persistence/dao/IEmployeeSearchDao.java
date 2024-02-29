package dev.magadiflo.criteria_queries.persistence.dao;

import dev.magadiflo.criteria_queries.persistence.entity.Employee;

import java.util.List;

public interface IEmployeeSearchDao {
    List<Employee> findAllBySimpleQuery(String firstName, String lastName, String email);
    List<Employee> findAllByDynamicQuery(String firstName, String lastName, String email);
}
