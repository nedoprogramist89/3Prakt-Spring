package com.mpt.journal.service;

import com.mpt.journal.entity.StudentProfile;
import com.mpt.journal.repository.StudentProfileRepository;
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
public class StudentProfileService {
    
    @Autowired
    private StudentProfileRepository studentProfileRepository;
    
    public StudentProfile save(StudentProfile profile) {
        return studentProfileRepository.save(profile);
    }
    
    public Optional<StudentProfile> findById(Long id) {
        return studentProfileRepository.findById(id);
    }
    
    public List<StudentProfile> findAll() {
        return studentProfileRepository.findByDeletedFalse();
    }
    
    public Page<StudentProfile> findAll(Pageable pageable) {
        return studentProfileRepository.findAll(pageable);
    }
    
    public void deleteById(Long id) {
        studentProfileRepository.deleteById(id);
    }
    
    public Page<StudentProfile> searchProfiles(String biography, String hobbies, String achievements,
                                               Boolean includeDeleted, int page, int size, 
                                               String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, 
            Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        
        return studentProfileRepository.findByParameters(biography, hobbies, achievements, 
                                                        includeDeleted, pageable);
    }
    
    public void softDelete(Long id) {
        Optional<StudentProfile> profile = studentProfileRepository.findById(id);
        if (profile.isPresent()) {
            profile.get().softDelete();
            studentProfileRepository.save(profile.get());
        }
    }
    
    public void restore(Long id) {
        Optional<StudentProfile> profile = studentProfileRepository.findById(id);
        if (profile.isPresent()) {
            profile.get().restore();
            studentProfileRepository.save(profile.get());
        }
    }
    
    public void bulkSoftDelete(List<Long> ids) {
        List<StudentProfile> profiles = studentProfileRepository.findAllById(ids);
        profiles.forEach(StudentProfile::softDelete);
        studentProfileRepository.saveAll(profiles);
    }
    
    public void bulkHardDelete(List<Long> ids) {
        studentProfileRepository.deleteAllById(ids);
    }
    
    public long count() {
        return studentProfileRepository.countByDeletedFalse();
    }
    
    public Optional<StudentProfile> findByStudentId(Long studentId) {
        return Optional.ofNullable(studentProfileRepository.findByStudentIdAndDeletedFalse(studentId));
    }
}
