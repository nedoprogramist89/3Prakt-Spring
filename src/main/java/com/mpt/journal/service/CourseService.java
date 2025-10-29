package com.mpt.journal.service;

import com.mpt.journal.entity.Course;
import com.mpt.journal.repository.CourseRepository;
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
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    public Course save(Course course) {
        return courseRepository.save(course);
    }
    
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }
    
    public List<Course> findAll() {
        return courseRepository.findByDeletedFalse();
    }
    
    public Page<Course> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }
    
    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }
    
    public Page<Course> searchCourses(String name, String description, Integer credits, 
                                     Integer hours, Long departmentId, Boolean includeDeleted,
                                     int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, 
            Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        
        return courseRepository.findByParameters(name, description, credits, hours, 
                                                departmentId, includeDeleted, pageable);
    }
    
    public void softDelete(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            course.get().softDelete();
            courseRepository.save(course.get());
        }
    }
    
    public void restore(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            course.get().restore();
            courseRepository.save(course.get());
        }
    }
    
    public void bulkSoftDelete(List<Long> ids) {
        List<Course> courses = courseRepository.findAllById(ids);
        courses.forEach(Course::softDelete);
        courseRepository.saveAll(courses);
    }
    
    public void bulkHardDelete(List<Long> ids) {
        courseRepository.deleteAllById(ids);
    }
    
    public long count() {
        return courseRepository.countByDeletedFalse();
    }
    
    public long countByDepartment(Long departmentId) {
        return courseRepository.countByDepartmentIdAndDeletedFalse(departmentId);
    }
    
    public List<Course> findByDepartment(Long departmentId) {
        return courseRepository.findByDepartmentIdAndDeletedFalse(departmentId);
    }
    
    public List<Course> findByCredits(Integer credits) {
        return courseRepository.findByCreditsAndDeletedFalse(credits);
    }
}
