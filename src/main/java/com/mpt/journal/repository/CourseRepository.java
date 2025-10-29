package com.mpt.journal.repository;

import com.mpt.journal.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    // Поиск по различным параметрам
    @Query("SELECT c FROM Course c WHERE " +
           "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:description IS NULL OR LOWER(c.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
           "(:credits IS NULL OR c.credits = :credits) AND " +
           "(:hours IS NULL OR c.hours = :hours) AND " +
           "(:departmentId IS NULL OR c.department.id = :departmentId) AND " +
           "(:includeDeleted = true OR c.deleted = false)")
    Page<Course> findByParameters(@Param("name") String name,
                                  @Param("description") String description,
                                  @Param("credits") Integer credits,
                                  @Param("hours") Integer hours,
                                  @Param("departmentId") Long departmentId,
                                  @Param("includeDeleted") Boolean includeDeleted,
                                  Pageable pageable);
    
    // Поиск активных курсов
    List<Course> findByDeletedFalse();
    
    // Поиск удаленных курсов
    List<Course> findByDeletedTrue();
    
    // Поиск по факультету
    List<Course> findByDepartmentIdAndDeletedFalse(Long departmentId);
    
    // Поиск по количеству кредитов
    List<Course> findByCreditsAndDeletedFalse(Integer credits);
    
    // Подсчет активных курсов
    long countByDeletedFalse();
    
    // Подсчет курсов в факультете
    long countByDepartmentIdAndDeletedFalse(Long departmentId);
}
