package dev.magadiflo.criteria_queries.persistence.dao;

import dev.magadiflo.criteria_queries.persistence.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class EmployeeSearchDao implements IEmployeeSearchDao {

    private final EntityManager entityManager;

    @Override
    public List<Employee> findAllBySimpleQuery(String firstName, String lastName, String email) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        // SELECT * FROM employees
        Root<Employee> root = criteriaQuery.from(Employee.class);

        // Prepare WHERE clause
        // first_name LIKE '%mar%'
        Predicate firstNamePredicate = criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%");
        // last_name LIKE '%mar%'
        Predicate lastNamePredicate = criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%");
        // email LIKE '%mar%'
        Predicate emailPredicate = criteriaBuilder.like(root.get("email"), "%" + email + "%");

        // WHERE first_name LIKE '%mar%' OR last_name LIKE '%mar%' OR email LIKE '%mar%'
        Predicate orPredicate = criteriaBuilder.or(firstNamePredicate, lastNamePredicate, emailPredicate);

        // Final query:
        //SELECT * FROM employees WHERE first_name LIKE '%mar%' OR last_name LIKE '%mar%' OR email LIKE '%mar%'
        criteriaQuery.where(orPredicate);

        TypedQuery<Employee> query = this.entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public List<Employee> findAllByDynamicQuery(String firstName, String lastName, String email) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        List<Predicate> predicates = new ArrayList<>();

        // SELECT * FROM employees
        Root<Employee> root = criteriaQuery.from(Employee.class);

        if (StringUtils.hasText(firstName)) {
            // first_name LIKE '%mar%'
            Predicate firstNamePredicate = criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%");
            predicates.add(firstNamePredicate);
        }

        if (StringUtils.hasText(lastName)) {
            /// last_name LIKE '%mar%'
            Predicate lastNamePredicate = criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%");
            predicates.add(lastNamePredicate);
        }

        if (StringUtils.hasText(email)) {
            // email LIKE '%mar%'
            Predicate emailPredicate = criteriaBuilder.like(root.get("email"), "%" + email + "%");
            predicates.add(emailPredicate);
        }

        if (!predicates.isEmpty()) {
            // SELECT * FROM employees WHERE first_name LIKE '%mar%' OR last_name LIKE '%mar%' OR email LIKE '%mar%'
            Predicate[] predicatesArray = predicates.toArray(new Predicate[0]); // Los predicates del ArrayList lo convertimos a un Array.
            Predicate orPredicate = criteriaBuilder.or(predicatesArray);
            criteriaQuery.where(orPredicate);
        }

        TypedQuery<Employee> query = this.entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

}
