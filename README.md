# [Spring Data JPA -Criteria Queries - The Full Guide](https://www.youtube.com/watch?v=qpSasUow1XI&t=24s)

- Tutorial tomado el canal de **Bouali Ali**
- [Proyecto donde se usa Specifications como capa superior de Criteria API](https://github.com/magadiflo/spring-boot-web-crud.git )

---

## Dependencias

````xml
<!--Spring Boot 3.2.3-->
<!--Java 21-->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
````

## Configuraciones

````yaml
server:
  port: 8080
  error:
    include-message: always

spring:
  application:
    name: spring-data-jpa-criteria-queries

  datasource:
    url: jdbc:mysql://localhost:3306/db_spring_data_jpa
    username: admin
    password: magadiflo

  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        format_sql: true

logging:
  level:
    org.hibernate.SQL: DEBUG
````

## Entidades

````java

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate hireDate;
    private Double salary;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
````

````java

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String phoneNumber;
}
````

## Tablas generadas

![tables](./assets/01.tables.png)

## Repositorios

````java
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
}
````

````java
public interface IDepartmentRepository extends JpaRepository<Department, Long> {
}
````

## Controladores

````java

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/employees")
public class EmployeeRestController {

    private final IEmployeeRepository employeeRepository;

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
}
````

````java

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
````

**ATENCIÓN**

> Por tema de desarrollo rápido del tutorial, es que uso directamente el `repository` en los controladores, pero en un
> desarrollo real, con buenas prácticas, se debería usar la capa de `service`.

## Ejecutando y probando endpoints

Lista de empleados:

````bash
$ curl -v http://localhost:8080/api/v1/employees | jq

>
< HTTP/1.1 200
<
[
  {
    "id": 1,
    "firstName": "Martín",
    "lastName": "Almagro",
    "email": "almagro@gmail.com",
    "phoneNumber": "963258969",
    "hireDate": "2015-03-01",
    "salary": 5000,
    "department": {
      "id": 1,
      "code": "D01",
      "name": "Sistemas",
      "phoneNumber": "332636"
    }
  },
  {...},
  {
    "id": 11,
    "firstName": "Judith",
    "lastName": "Alegría",
    "email": "ciro@gmail.com",
    "phoneNumber": "943851697",
    "hireDate": "2015-03-29",
    "salary": 5455,
    "department": {
      "id": 5,
      "code": "D05",
      "name": "Soporte",
      "phoneNumber": "321478"
    }
  }
]
````

Lista de departamentos:

````bash
$ curl -v http://localhost:8080/api/v1/departments | jq

>
< HTTP/1.1 200
[
  {
    "id": 1,
    "code": "D01",
    "name": "Sistemas",
    "phoneNumber": "332636"
  },
  {...},
  {
    "id": 5,
    "code": "D05",
    "name": "Soporte",
    "phoneNumber": "321478"
  }
]
````

## Criteria API

El `API de Criteria` de Spring Data JPA es una característica que proporciona Spring Data JPA para construir consultas
de manera programática utilizando un conjunto de clases y métodos definidos en el paquete `javax.persistence.criteria`.

Este **API permite a los desarrolladores construir consultas de manera dinámica y flexible en tiempo de ejecución, lo
que es especialmente útil cuando las consultas pueden variar según ciertas condiciones o criterios.**

Algunas características y ventajas del API de Criteria de Spring Data JPA incluyen:

1. `Construcción de consultas de manera programática:` En lugar de escribir consultas JPQL (Java Persistence Query
   Language) estáticas en forma de cadenas, el API de Criteria permite construir consultas utilizando clases y métodos
   Java, lo que facilita la creación de consultas dinámicas y comprensibles desde el punto de vista del código.


2. `Seguridad contra errores de sintaxis:` Al utilizar el API de Criteria, los errores de sintaxis en las consultas se
   detectan en tiempo de compilación, lo que evita errores comunes que podrían ocurrir al escribir consultas JPQL
   manualmente.


3. `Soporte para consultas complejas:` El API de Criteria admite la construcción de consultas complejas que pueden
   incluir múltiples condiciones, joins, ordenamientos y agrupamientos, lo que permite a los desarrolladores expresar
   consultas sofisticadas de manera programática.


4. `Compatibilidad con múltiples proveedores de JPA:` El API de Criteria es compatible con múltiples proveedores de JPA,
   lo que significa que puede utilizarse con implementaciones de JPA como Hibernate, EclipseLink, OpenJPA, entre otros.

## Implementando Criteria API - 1° Ejemplo

Creamos una clase `DAO` al cual debemos anotar con `@Repository`, ya que nuestra clase interactuará con la base de datos
y además para que Spring lo detecte.

````java
public interface IEmployeeSearchDao {
    List<Employee> findAllBySimpleQuery(String firstName, String lastName, String email);
}
````

````java

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

}
````

Ahora, creamos el endpoint correspondiente en su controlador:

````java

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/employees")
public class EmployeeRestController {

    private final IEmployeeRepository employeeRepository;
    private final IEmployeeSearchDao employeeSearchDao;

    /* other endpoints */

    @GetMapping(path = "/search-simple")
    public ResponseEntity<List<Employee>> getAllEmployeesSearchSimple(@RequestParam String firstName,
                                                                      @RequestParam String lastName,
                                                                      @RequestParam String email) {
        return ResponseEntity.ok(this.employeeSearchDao.findAllBySimpleQuery(firstName, lastName, email));
    }
}
````

**IMPORTANTE**

> Cuando llamemos al endpoint `/search-simple`, debemos pasarle como parámetros en la url las
> variables `firstName`, `lastName` e `email`, sí o sí, debemos pasarle los tres términos de búsqueda. Para este primer
> ejemplo, lo haremos así, pero recordemos que precisamente hacemos uso de del API de Criteria para crear dinámicamente
> las condiciones de búsqueda, algo que aquí no se está dando, ya que sí o sí debemos pasar los tres términos. Para ser
> el primer ejemplo está bien, pero en el segundo ejemplo mejoraremos dicha consulta, lo haremos más dinámico.

## Resultados - 1° ejemplo

Realizamos una petición al endpoint cuyo dao hace uso del Criteria API:

````bash
$ curl -v -G --data "firstName=estela&lastName=vega&email=ciro" http://localhost:8080/api/v1/employees/search-simple | jq

>
< HTTP/1.1 200
[
  {
    "id": 8,
    "firstName": "Carmen",
    "lastName": "Vega",
    "email": "vega@gmail.com",
    "phoneNumber": "963258961",
    "hireDate": "2015-06-18",
    "salary": 1900,
    "department": {
      "id": 4,
      "code": "D04",
      "name": "Recursos Humanos",
      "phoneNumber": "378965"
    }
  },
  {
    "id": 9,
    "firstName": "Estela",
    "lastName": "Sullón",
    "email": "estela@gmail.com",
    "phoneNumber": "943852525",
    "hireDate": "2020-03-15",
    "salary": 2600,
    "department": {
      "id": 4,
      "code": "D04",
      "name": "Recursos Humanos",
      "phoneNumber": "378965"
    }
  },
  {
    "id": 11,
    "firstName": "Judith",
    "lastName": "Alegría",
    "email": "ciro@gmail.com",
    "phoneNumber": "943851697",
    "hireDate": "2015-03-29",
    "salary": 5455,
    "department": {
      "id": 5,
      "code": "D05",
      "name": "Soporte",
      "phoneNumber": "321478"
    }
  }
]
````

La consulta generada por `Spring Data JPA` es la siguiente:

````bash
2024-02-28T17:32:58.062-05:00 DEBUG 7624 --- [spring-data-jpa-criteria-queries] [nio-8080-exec-3] org.hibernate.SQL                        : 
    select
        e1_0.id,
        e1_0.department_id,
        e1_0.email,
        e1_0.first_name,
        e1_0.hire_date,
        e1_0.last_name,
        e1_0.phone_number,
        e1_0.salary 
    from
        employees e1_0 
    where
        e1_0.first_name like replace(?, '\\', '\\\\') 
        or e1_0.last_name like replace(?, '\\', '\\\\') 
        or e1_0.email like replace(?, '\\', '\\\\')
2024-02-28T17:32:58.067-05:00 DEBUG 7624 --- [spring-data-jpa-criteria-queries] [nio-8080-exec-3] org.hibernate.SQL                        : 
    select
        d1_0.id,
        d1_0.code,
        d1_0.name,
        d1_0.phone_number 
    from
        departments d1_0 
    where
        d1_0.id=?
2024-02-28T17:32:58.071-05:00 DEBUG 7624 --- [spring-data-jpa-criteria-queries] [nio-8080-exec-3] org.hibernate.SQL                        : 
    select
        d1_0.id,
        d1_0.code,
        d1_0.name,
        d1_0.phone_number 
    from
        departments d1_0 
    where
        d1_0.id=?
````

## Implementando Criteria API (dinámico) - 2° Ejemplo

En el ejemplo anterior necesitábamos mandar sí o sí todos los parámetros de consulta, pero qué pasa si solo queremos
consultar por un parámetro, o por dos o por los tres o por ninguno, es decir, la consulta debería ser dinámica, eso
es lo que lograremos en este ejemplo.

Creamos un nuevo método en nuestro DAO:

````java
public interface IEmployeeSearchDao {
    List<Employee> findAllBySimpleQuery(String firstName, String lastName, String email);

    List<Employee> findAllByDynamicQuery(String firstName, String lastName, String email);
}
````

El nuevo método que creamos tiene la misma funcionalidad que el primero, la diferencia está en las validaciones que
hacemos de los parámetros, en este caso hacemos uso de la clase de spring `StringUtils.hasText()`. Si pasa la validación
recién creamos el `predicate` para el parámetro correspondiente y lo almacenamos en una lista de predicados. Al final,
validamos si la lista no está vacía para poder crear el `WHERE` de la consulta:

````java

@RequiredArgsConstructor
@Repository
public class EmployeeSearchDao implements IEmployeeSearchDao {

    private final EntityManager entityManager;

    /* other method */

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
````

Implementamos el nuevo endpoint para realizar las consultas dinámicas. **Notar que los parámetros ahora son opcionales,
ya que configuramos la propiedad `required=false`:**

````java

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/employees")
public class EmployeeRestController {

    private final IEmployeeRepository employeeRepository;
    private final IEmployeeSearchDao employeeSearchDao;

    /* other methods */

    @GetMapping(path = "/search-dynamic")
    public ResponseEntity<List<Employee>> getAllEmployeesSearchDynamic(@RequestParam(required = false) String firstName,
                                                                       @RequestParam(required = false) String lastName,
                                                                       @RequestParam(required = false) String email) {
        return ResponseEntity.ok(this.employeeSearchDao.findAllByDynamicQuery(firstName, lastName, email));
    }
}
````

## Resultados - 2° ejemplo dinámico

### Realizando request sin parámetros

Al llamar al endpoint sin parámetros, vemos que nos trae todos los registros de la tabla, ya que no hay condición:

````bash
$ curl -v http://localhost:8080/api/v1/employees/search-dynamic | jq

>
< HTTP/1.1 200
<
[
  {
    "id": 1,
    "firstName": "Martín",
    "lastName": "Almagro",
    "email": "almagro@gmail.com",
    "phoneNumber": "963258969",
    "hireDate": "2015-03-01",
    "salary": 5000,
    "department": {
      "id": 1,
      "code": "D01",
      "name": "Sistemas",
      "phoneNumber": "332636"
    }
  },
  {...}
  {
    "id": 11,
    "firstName": "Judith",
    "lastName": "Alegría",
    "email": "ciro@gmail.com",
    "phoneNumber": "943851697",
    "hireDate": "2015-03-29",
    "salary": 5455,
    "department": {
      "id": 5,
      "code": "D05",
      "name": "Soporte",
      "phoneNumber": "321478"
    }
  }
]
````

### Realizando request por el firstName

````bash
$ curl -v -G --data "firstName=u" http://localhost:8080/api/v1/employees/search-dynamic | jq

>
< HTTP/1.1 200
<
[
  {
    "id": 2,
    "firstName": "Lucía",
    "lastName": "Campos",
    "email": "lucia@gmail.com",
    "phoneNumber": "985478969",
    "hireDate": "2020-05-15",
    "salary": 2500,
    "department": {
      "id": 1,
      "code": "D01",
      "name": "Sistemas",
      "phoneNumber": "332636"
    }
  },
  {
    "id": 11,
    "firstName": "Judith",
    "lastName": "Alegría",
    "email": "ciro@gmail.com",
    "phoneNumber": "943851697",
    "hireDate": "2015-03-29",
    "salary": 5455,
    "department": {
      "id": 5,
      "code": "D05",
      "name": "Soporte",
      "phoneNumber": "321478"
    }
  }
]
````

### Realizando request por el fistName y lastName

````bash
$ curl -v -G --data "firstName=u&lastName=doza" http://localhost:8080/api/v1/employees/search-dynamic | jq

>
< HTTP/1.1 200
<
[
  {
    "id": 2,
    "firstName": "Lucía",
    "lastName": "Campos",
    "email": "lucia@gmail.com",
    "phoneNumber": "985478969",
    "hireDate": "2020-05-15",
    "salary": 2500,
    "department": {
      "id": 1,
      "code": "D01",
      "name": "Sistemas",
      "phoneNumber": "332636"
    }
  },
  {
    "id": 6,
    "firstName": "María",
    "lastName": "Mendoza",
    "email": "mendoza@gmail.com",
    "phoneNumber": "913589698",
    "hireDate": "2013-09-25",
    "salary": 2900,
    "department": {
      "id": 3,
      "code": "D03",
      "name": "Contabilidad",
      "phoneNumber": "332514"
    }
  },
  {
    "id": 11,
    "firstName": "Judith",
    "lastName": "Alegría",
    "email": "ciro@gmail.com",
    "phoneNumber": "943851697",
    "hireDate": "2015-03-29",
    "salary": 5455,
    "department": {
      "id": 5,
      "code": "D05",
      "name": "Soporte",
      "phoneNumber": "321478"
    }
  }
]
````

### Realizando request por firstName, lastName e email

````bash
$ curl -v -G --data "firstName=arely&lastName=caldas&email=arely" http://localhost:8080/api/v1/employees/search-dynamic | jq

>
< HTTP/1.1 200
<
[
  {
    "id": 3,
    "firstName": "Arely",
    "lastName": "Caldas",
    "email": "arely@gmail.com",
    "phoneNumber": "985965896",
    "hireDate": "2002-12-08",
    "salary": 3650,
    "department": {
      "id": 2,
      "code": "D02",
      "name": "Administración",
      "phoneNumber": "325089"
    }
  }
]
````
