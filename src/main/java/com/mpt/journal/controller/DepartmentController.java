package com.mpt.journal.controller;

import com.mpt.journal.entity.Department;
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
@RequestMapping("/departments")
public class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;
    
    @GetMapping
    public String getAllDepartments(Model model,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String description,
                                   @RequestParam(required = false) String address,
                                   @RequestParam(required = false) String phone,
                                   @RequestParam(required = false, defaultValue = "false") Boolean includeDeleted,
                                   @RequestParam(required = false, defaultValue = "0") Integer page,
                                   @RequestParam(required = false, defaultValue = "10") Integer size,
                                   @RequestParam(required = false, defaultValue = "id") String sortBy,
                                   @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        
        Page<Department> departments = departmentService.searchDepartments(name, description, address, phone, 
                                                                          includeDeleted,
                                                                          page, size, sortBy, sortDir);
        
        model.addAttribute("departments", departments.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", departments.getTotalPages());
        model.addAttribute("totalElements", departments.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
        // Параметры поиска
        model.addAttribute("name", name);
        model.addAttribute("description", description);
        model.addAttribute("address", address);
        model.addAttribute("phone", phone);
        model.addAttribute("includeDeleted", includeDeleted);
        
        return "departments/list";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("department", new Department());
        return "departments/add";
    }
    
    @PostMapping("/add")
    public String addDepartment(@Valid @ModelAttribute Department department, 
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "departments/add";
        }
        
        departmentService.save(department);
        return "redirect:/departments";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Department department = departmentService.findById(id).orElse(null);
        if (department == null) {
            return "redirect:/departments";
        }
        
        model.addAttribute("department", department);
        return "departments/edit";
    }
    
    @PostMapping("/edit")
    public String updateDepartment(@Valid @ModelAttribute Department department, 
                                  BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "departments/edit";
        }
        
        departmentService.save(department);
        return "redirect:/departments";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteById(id);
        return "redirect:/departments";
    }
    
    @PostMapping("/soft-delete/{id}")
    public String softDeleteDepartment(@PathVariable Long id) {
        departmentService.softDelete(id);
        return "redirect:/departments";
    }
    
    @PostMapping("/restore/{id}")
    public String restoreDepartment(@PathVariable Long id) {
        departmentService.restore(id);
        return "redirect:/departments";
    }
    
    @PostMapping("/bulk-soft-delete")
    public String bulkSoftDelete(@RequestParam(value = "ids", required = false) List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            departmentService.bulkSoftDelete(ids);
        }
        return "redirect:/departments";
    }
    
    @PostMapping("/bulk-hard-delete")
    public String bulkHardDelete(@RequestParam(value = "ids", required = false) List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            departmentService.bulkHardDelete(ids);
        }
        return "redirect:/departments";
    }
    
    @GetMapping("/view/{id}")
    public String viewDepartment(@PathVariable Long id, Model model) {
        Department department = departmentService.findById(id).orElse(null);
        if (department == null) {
            return "redirect:/departments";
        }
        
        model.addAttribute("department", department);
        return "departments/view";
    }
}
