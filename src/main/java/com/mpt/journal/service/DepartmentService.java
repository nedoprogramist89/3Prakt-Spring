package com.mpt.journal.service;

import com.mpt.journal.entity.Department;
import com.mpt.journal.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartmentService {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    // CRUD операции
    public Department save(Department department) {
        return departmentRepository.save(department);
    }
    
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }
    
    public List<Department> findAll() {
        return departmentRepository.findByDeletedFalse();
    }
    
    public Page<Department> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }
    
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }
    
    // Поиск и фильтрация
    public Page<Department> searchDepartments(String name, String description, String address, 
                                             String phone, Boolean includeDeleted,
                                             int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, 
            Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        
        return departmentRepository.findByParameters(name, description, address, phone, 
                                                    includeDeleted, pageable);
    }
    
    // Логическое удаление
    public void softDelete(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            department.get().softDelete();
            departmentRepository.save(department.get());
        }
    }
    
    public void restore(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            department.get().restore();
            departmentRepository.save(department.get());
        }
    }
    
    public void bulkSoftDelete(List<Long> ids) {
        List<Department> departments = departmentRepository.findAllById(ids);
        departments.forEach(Department::softDelete);
        departmentRepository.saveAll(departments);
    }
    
    public void bulkHardDelete(List<Long> ids) {
        departmentRepository.deleteAllById(ids);
    }
    
    // Статистика
    public long count() {
        return departmentRepository.countByDeletedFalse();
    }
    
    // Поиск по названию
    public Optional<Department> findByName(String name) {
        return Optional.ofNullable(departmentRepository.findByNameAndDeletedFalse(name));
    }
}
