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

    Optional<Employee> findByName(String name);

    // Paged queries
    Page<Employee> findByDepartmentId(Long deptId, Pageable pageable);
    Page<Employee> findByBirthDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Employee> findByDepartmentIdAndBirthDateBetween(Long deptId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    // Global averages
    @Query(value = "SELECT AVG(salary) FROM employees", nativeQuery = true)
    Double calculateAverageSalaryAll();

    @Query(value = "SELECT AVG(YEAR(CURDATE()) - YEAR(birth_date) - " +
            "(DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birth_date, '%m%d'))) " +
            "FROM employees", nativeQuery = true)
    Double calculateAverageAgeAll();

    // Department averages
    @Query(value = "SELECT AVG(salary) FROM employees WHERE department_id = :deptId", nativeQuery = true)
    Double calculateAverageSalaryByDepartment(@Param("deptId") Long deptId);

    @Query(value = "SELECT AVG(YEAR(CURDATE()) - YEAR(birth_date) - " +
            "(DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birth_date, '%m%d'))) " +
            "FROM employees WHERE department_id = :deptId", nativeQuery = true)
    Double calculateAverageAgeByDepartment(@Param("deptId") Long deptId);

    // Age range averages (birthDate range derived in service)
    @Query(value = "SELECT AVG(salary) FROM employees WHERE birth_date BETWEEN :minBirthDate AND :maxBirthDate", nativeQuery = true)
    Double calculateAverageSalaryByAgeRange(@Param("minBirthDate") LocalDate minBirthDate,
                                            @Param("maxBirthDate") LocalDate maxBirthDate);

    @Query(value = "SELECT AVG(YEAR(CURDATE()) - YEAR(birth_date) - " +
            "(DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birth_date, '%m%d'))) " +
            "FROM employees WHERE birth_date BETWEEN :minBirthDate AND :maxBirthDate", nativeQuery = true)
    Double calculateAverageAgeByRange(@Param("minBirthDate") LocalDate minBirthDate,
                                      @Param("maxBirthDate") LocalDate maxBirthDate);

    // Department + age range
    @Query(value = "SELECT AVG(salary) FROM employees WHERE department_id = :deptId AND birth_date BETWEEN :minBirthDate AND :maxBirthDate", nativeQuery = true)
    Double calculateAverageSalaryByDepartmentAndAgeRange(@Param("deptId") Long deptId,
                                                         @Param("minBirthDate") LocalDate minBirthDate,
                                                         @Param("maxBirthDate") LocalDate maxBirthDate);

    @Query(value = "SELECT AVG(YEAR(CURDATE()) - YEAR(birth_date) - " +
            "(DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birth_date, '%m%d'))) " +
            "FROM employees WHERE department_id = :deptId AND birth_date BETWEEN :minBirthDate AND :maxBirthDate", nativeQuery = true)
    Double calculateAverageAgeByDepartmentAndAgeRange(@Param("deptId") Long deptId,
                                                      @Param("minBirthDate") LocalDate minBirthDate,
                                                      @Param("maxBirthDate") LocalDate maxBirthDate);
}
