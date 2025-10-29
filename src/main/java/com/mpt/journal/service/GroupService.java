package com.mpt.journal.service;

import com.mpt.journal.entity.Group;
import com.mpt.journal.repository.GroupRepository;
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
public class GroupService {
    
    @Autowired
    private GroupRepository groupRepository;
    
    public Group save(Group group) {
        return groupRepository.save(group);
    }
    
    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }
    
    public List<Group> findAll() {
        return groupRepository.findByDeletedFalse();
    }
    
    public Page<Group> findAll(Pageable pageable) {
        return groupRepository.findAll(pageable);
    }
    
    public void deleteById(Long id) {
        groupRepository.deleteById(id);
    }
    
    public Page<Group> searchGroups(String name, String description, Integer year, 
                                   Long departmentId, Boolean includeDeleted,
                                   int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, 
            Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        
        return groupRepository.findByParameters(name, description, year, 
                                               departmentId, includeDeleted, pageable);
    }
    
    public void softDelete(Long id) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            group.get().softDelete();
            groupRepository.save(group.get());
        }
    }
    
    public void restore(Long id) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            group.get().restore();
            groupRepository.save(group.get());
        }
    }
    
    public void bulkSoftDelete(List<Long> ids) {
        List<Group> groups = groupRepository.findAllById(ids);
        groups.forEach(Group::softDelete);
        groupRepository.saveAll(groups);
    }
    
    public void bulkHardDelete(List<Long> ids) {
        groupRepository.deleteAllById(ids);
    }
    
    public long count() {
        return groupRepository.countByDeletedFalse();
    }
    
    public long countByDepartment(Long departmentId) {
        return groupRepository.countByDepartmentIdAndDeletedFalse(departmentId);
    }
    
    public List<Group> findByDepartment(Long departmentId) {
        return groupRepository.findByDepartmentIdAndDeletedFalse(departmentId);
    }
    
    public List<Group> findByYear(Integer year) {
        return groupRepository.findByYearAndDeletedFalse(year);
    }
}
