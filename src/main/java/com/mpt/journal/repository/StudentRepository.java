package com.mpt.journal.repository;

import com.mpt.journal.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Поиск по различным параметрам
    @Query("SELECT s FROM Student s WHERE " +
           "(:firstName IS NULL OR LOWER(s.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
           "(:lastName IS NULL OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
           "(:middleName IS NULL OR LOWER(s.middleName) LIKE LOWER(CONCAT('%', :middleName, '%'))) AND " +
           "(:email IS NULL OR LOWER(s.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:groupId IS NULL OR s.group.id = :groupId) AND " +
           "(:includeDeleted = true OR s.deleted = false)")
    Page<Student> findByParameters(@Param("firstName") String firstName,
                                   @Param("lastName") String lastName,
                                   @Param("middleName") String middleName,
                                   @Param("email") String email,
                                   @Param("groupId") Long groupId,
                                   @Param("includeDeleted") Boolean includeDeleted,
                                   Pageable pageable);
    
    // Поиск активных студентов
    List<Student> findByDeletedFalse();
    
    // Поиск удаленных студентов
    List<Student> findByDeletedTrue();
    
    // Поиск по группе
    List<Student> findByGroupIdAndDeletedFalse(Long groupId);
    
    // Поиск по email
    Student findByEmailAndDeletedFalse(String email);
    
    // Подсчет активных студентов
    long countByDeletedFalse();
    
    // Подсчет студентов в группе
    long countByGroupIdAndDeletedFalse(Long groupId);
}
