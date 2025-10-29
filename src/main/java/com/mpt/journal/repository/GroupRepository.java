package com.mpt.journal.repository;

import com.mpt.journal.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    
    @Query("SELECT g FROM Group g WHERE " +
           "(:name IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:description IS NULL OR LOWER(g.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
           "(:year IS NULL OR g.year = :year) AND " +
           "(:departmentId IS NULL OR g.department.id = :departmentId) AND " +
           "(:includeDeleted = true OR g.deleted = false)")
    Page<Group> findByParameters(@Param("name") String name,
                                 @Param("description") String description,
                                 @Param("year") Integer year,
                                 @Param("departmentId") Long departmentId,
                                 @Param("includeDeleted") Boolean includeDeleted,
                                 Pageable pageable);
    
    List<Group> findByDeletedFalse();
    
    List<Group> findByDeletedTrue();
    
    List<Group> findByDepartmentIdAndDeletedFalse(Long departmentId);
    
    List<Group> findByYearAndDeletedFalse(Integer year);
    
    long countByDeletedFalse();
    
    long countByDepartmentIdAndDeletedFalse(Long departmentId);
}
