package com.mpt.journal.controller;

import com.mpt.journal.entity.Group;
import com.mpt.journal.entity.Department;
import com.mpt.journal.service.GroupService;
import com.mpt.journal.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupController {
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private DepartmentService departmentService;
    
    @GetMapping
    public String getAllGroups(Model model,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String description,
                              @RequestParam(required = false) Integer year,
                              @RequestParam(required = false) Long departmentId,
                              @RequestParam(required = false, defaultValue = "false") Boolean includeDeleted,
                              @RequestParam(required = false, defaultValue = "0") Integer page,
                              @RequestParam(required = false, defaultValue = "10") Integer size,
                              @RequestParam(required = false, defaultValue = "id") String sortBy,
                              @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        
        Page<Group> groups = groupService.searchGroups(name, description, year, 
                                                      departmentId, includeDeleted,
                                                      page, size, sortBy, sortDir);
        
        List<Department> departments = departmentService.findAll();
        
        model.addAttribute("groups", groups.getContent());
        model.addAttribute("departments", departments);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", groups.getTotalPages());
        model.addAttribute("totalElements", groups.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
        // Параметры поиска
        model.addAttribute("name", name);
        model.addAttribute("description", description);
        model.addAttribute("year", year);
        model.addAttribute("departmentId", departmentId);
        model.addAttribute("includeDeleted", includeDeleted);
        
        return "groups/list";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("group", new Group());
        model.addAttribute("departments", departmentService.findAll());
        return "groups/add";
    }
    
    @PostMapping("/add")
    public String addGroup(@Valid @ModelAttribute Group group, 
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            return "groups/add";
        }
        
        groupService.save(group);
        return "redirect:/groups";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Group group = groupService.findById(id).orElse(null);
        if (group == null) {
            return "redirect:/groups";
        }
        
        model.addAttribute("group", group);
        model.addAttribute("departments", departmentService.findAll());
        return "groups/edit";
    }
    
    @PostMapping("/edit")
    public String updateGroup(@Valid @ModelAttribute Group group, 
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            return "groups/edit";
        }
        
        groupService.save(group);
        return "redirect:/groups";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteGroup(@PathVariable Long id) {
        groupService.deleteById(id);
        return "redirect:/groups";
    }
    
    @PostMapping("/soft-delete/{id}")
    public String softDeleteGroup(@PathVariable Long id) {
        groupService.softDelete(id);
        return "redirect:/groups";
    }
    
    @PostMapping("/restore/{id}")
    public String restoreGroup(@PathVariable Long id) {
        groupService.restore(id);
        return "redirect:/groups";
    }
    
    @PostMapping("/bulk-soft-delete")
    public String bulkSoftDelete(@RequestParam(value = "ids", required = false) List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            groupService.bulkSoftDelete(ids);
        }
        return "redirect:/groups";
    }
    
    @PostMapping("/bulk-hard-delete")
    public String bulkHardDelete(@RequestParam(value = "ids", required = false) List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            groupService.bulkHardDelete(ids);
        }
        return "redirect:/groups";
    }
    
    @GetMapping("/view/{id}")
    public String viewGroup(@PathVariable Long id, Model model) {
        Group group = groupService.findById(id).orElse(null);
        if (group == null) {
            return "redirect:/groups";
        }
        
        model.addAttribute("group", group);
        return "groups/view";
    }
}
