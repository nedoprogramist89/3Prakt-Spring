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
    
    List<Student> findByDeletedFalse();
    
    List<Student> findByDeletedTrue();
    
    List<Student> findByGroupIdAndDeletedFalse(Long groupId);
    
    Student findByEmailAndDeletedFalse(String email);
    
    long countByDeletedFalse();
    
    long countByGroupIdAndDeletedFalse(Long groupId);
}
