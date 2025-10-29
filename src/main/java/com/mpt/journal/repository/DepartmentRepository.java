package com.mpt.journal.repository;

import com.mpt.journal.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    // Поиск по различным параметрам
    @Query("SELECT d FROM Department d WHERE " +
           "(:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:description IS NULL OR LOWER(d.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
           "(:address IS NULL OR LOWER(d.address) LIKE LOWER(CONCAT('%', :address, '%'))) AND " +
           "(:phone IS NULL OR LOWER(d.phone) LIKE LOWER(CONCAT('%', :phone, '%'))) AND " +
           "(:includeDeleted = true OR d.deleted = false)")
    Page<Department> findByParameters(@Param("name") String name,
                                      @Param("description") String description,
                                      @Param("address") String address,
                                      @Param("phone") String phone,
                                      @Param("includeDeleted") Boolean includeDeleted,
                                      Pageable pageable);
    
    // Поиск активных факультетов
    List<Department> findByDeletedFalse();
    
    // Поиск удаленных факультетов
    List<Department> findByDeletedTrue();
    
    // Поиск по названию
    Department findByNameAndDeletedFalse(String name);
    
    // Подсчет активных факультетов
    long countByDeletedFalse();
}
