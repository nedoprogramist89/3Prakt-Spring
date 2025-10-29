package com.mpt.journal.repository;

import com.mpt.journal.entity.StudentProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    
    // Поиск по различным параметрам
    @Query("SELECT sp FROM StudentProfile sp WHERE " +
           "(:biography IS NULL OR LOWER(sp.biography) LIKE LOWER(CONCAT('%', :biography, '%'))) AND " +
           "(:hobbies IS NULL OR LOWER(sp.hobbies) LIKE LOWER(CONCAT('%', :hobbies, '%'))) AND " +
           "(:achievements IS NULL OR LOWER(sp.achievements) LIKE LOWER(CONCAT('%', :achievements, '%'))) AND " +
           "(:includeDeleted = true OR sp.deleted = false)")
    Page<StudentProfile> findByParameters(@Param("biography") String biography,
                                          @Param("hobbies") String hobbies,
                                          @Param("achievements") String achievements,
                                          @Param("includeDeleted") Boolean includeDeleted,
                                          Pageable pageable);
    
    // Поиск активных профилей
    List<StudentProfile> findByDeletedFalse();
    
    // Поиск удаленных профилей
    List<StudentProfile> findByDeletedTrue();
    
    // Поиск по студенту
    StudentProfile findByStudentIdAndDeletedFalse(Long studentId);
    
    // Подсчет активных профилей
    long countByDeletedFalse();
}
