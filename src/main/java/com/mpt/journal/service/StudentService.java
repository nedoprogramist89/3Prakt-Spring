package com.mpt.journal.service;

import com.mpt.journal.entity.Student;
import com.mpt.journal.repository.StudentRepository;
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
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    public Student save(Student student) {
        return studentRepository.save(student);
    }
    
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }
    
    public List<Student> findAll() {
        return studentRepository.findByDeletedFalse();
    }
    
    public Page<Student> findAll(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }
    
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
    
    public Page<Student> searchStudents(String firstName, String lastName, String middleName, 
                                       String email, Long groupId, Boolean includeDeleted,
                                       int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, 
            Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        
        return studentRepository.findByParameters(firstName, lastName, middleName, 
                                                 email, groupId, includeDeleted, pageable);
    }
    
    public void softDelete(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            student.get().softDelete();
            studentRepository.save(student.get());
        }
    }
    
    public void restore(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            student.get().restore();
            studentRepository.save(student.get());
        }
    }
    
    public void bulkSoftDelete(List<Long> ids) {
        List<Student> students = studentRepository.findAllById(ids);
        students.forEach(Student::softDelete);
        studentRepository.saveAll(students);
    }
    
    public void bulkHardDelete(List<Long> ids) {
        studentRepository.deleteAllById(ids);
    }
    
    public long count() {
        return studentRepository.countByDeletedFalse();
    }
    
    public long countByGroup(Long groupId) {
        return studentRepository.countByGroupIdAndDeletedFalse(groupId);
    }
    
    public List<Student> findByGroup(Long groupId) {
        return studentRepository.findByGroupIdAndDeletedFalse(groupId);
    }
    
    public Optional<Student> findByEmail(String email) {
        return Optional.ofNullable(studentRepository.findByEmailAndDeletedFalse(email));
    }
}
