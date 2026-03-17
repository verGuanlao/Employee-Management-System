package com.example.employeemanagementsystem.repository;

import com.example.employeemanagementsystem.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Getting single employee
    Page<Employee> findByNameContainingIgnoreCase(String name,  Pageable pageable);


    // Paged queries
    Page<Employee> findByDepartmentDepartmentId(Long deptId, Pageable pageable);
    Page<Employee> findByBirthDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Employee> findByDepartmentDepartmentIdAndBirthDateBetween(Long deptId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    // Global averages
    @Query(value = "SELECT AVG(employee_salary) FROM employee", nativeQuery = true)
    Double calculateAverageSalaryAll();

    @Query(value = "SELECT AVG(YEAR(CURDATE()) - YEAR(birth_date) - " +
            "(DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birth_date, '%m%d'))) " +
            "FROM employee", nativeQuery = true)
    Double calculateAverageAgeAll();

    // Department averages
    @Query(value = "SELECT AVG(employee_salary) FROM employee WHERE department_id = :deptId", nativeQuery = true)
    Double calculateAverageSalaryByDepartment(@Param("deptId") Long deptId);

    @Query(value = "SELECT AVG(YEAR(CURDATE()) - YEAR(birth_date) - " +
            "(DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birth_date, '%m%d'))) " +
            "FROM employee WHERE department_id = :deptId", nativeQuery = true)
    Double calculateAverageAgeByDepartment(@Param("deptId") Long deptId);

    // Age range averages (birthDate range derived in service)
    @Query(value = "SELECT AVG(employee_salary) FROM employee WHERE birth_date BETWEEN :minBirthDate AND :maxBirthDate", nativeQuery = true)
    Double calculateAverageSalaryByAgeRange(@Param("minBirthDate") LocalDate minBirthDate,
                                            @Param("maxBirthDate") LocalDate maxBirthDate);

    @Query(value = "SELECT AVG(YEAR(CURDATE()) - YEAR(birth_date) - " +
            "(DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birth_date, '%m%d'))) " +
            "FROM employee WHERE birth_date BETWEEN :minBirthDate AND :maxBirthDate", nativeQuery = true)
    Double calculateAverageAgeByRange(@Param("minBirthDate") LocalDate minBirthDate,
                                      @Param("maxBirthDate") LocalDate maxBirthDate);

    // Department + age range
    @Query(value = "SELECT AVG(employee_salary) FROM employee WHERE department_id = :deptId AND birth_date BETWEEN :minBirthDate AND :maxBirthDate", nativeQuery = true)
    Double calculateAverageSalaryByDepartmentAndAgeRange(@Param("deptId") Long deptId,
                                                         @Param("minBirthDate") LocalDate minBirthDate,
                                                         @Param("maxBirthDate") LocalDate maxBirthDate);

    @Query(value = "SELECT AVG(YEAR(CURDATE()) - YEAR(birth_date) - " +
            "(DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birth_date, '%m%d'))) " +
            "FROM employee WHERE department_id = :deptId AND birth_date BETWEEN :minBirthDate AND :maxBirthDate", nativeQuery = true)
    Double calculateAverageAgeByDepartmentAndAgeRange(@Param("deptId") Long deptId,
                                                      @Param("minBirthDate") LocalDate minBirthDate,
                                                      @Param("maxBirthDate") LocalDate maxBirthDate);

    // Count all employees
    @Query(value = "SELECT COUNT(*) FROM employee", nativeQuery = true)
    Long countAllEmployees();

    // Count employees by department
    @Query(value = "SELECT COUNT(*) FROM employee WHERE department_id = :deptId", nativeQuery = true)
    Long countEmployeesByDepartment(@Param("deptId") Long deptId);

    // Count employees by age range (birthDate range derived in service)
    @Query(value = "SELECT COUNT(*) FROM employee WHERE birth_date BETWEEN :minBirthDate AND :maxBirthDate", nativeQuery = true)
    Long countEmployeesByAgeRange(@Param("minBirthDate") LocalDate minBirthDate,
                                  @Param("maxBirthDate") LocalDate maxBirthDate);

    // Count employees by department and age range
    @Query(value = "SELECT COUNT(*) FROM employee WHERE department_id = :deptId AND birth_date BETWEEN :minBirthDate AND :maxBirthDate", nativeQuery = true)
    Long countEmployeesByDepartmentAndAgeRange(@Param("deptId") Long deptId,
                                               @Param("minBirthDate") LocalDate minBirthDate,
                                               @Param("maxBirthDate") LocalDate maxBirthDate);

}
