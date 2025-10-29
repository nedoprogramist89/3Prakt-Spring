package com.mpt.journal.controller;

import com.mpt.journal.entity.Student;
import com.mpt.journal.entity.Group;
import com.mpt.journal.service.StudentService;
import com.mpt.journal.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private GroupService groupService;
    
    @GetMapping
    public String getAllStudents(Model model,
                                 @RequestParam(required = false) String firstName,
                                 @RequestParam(required = false) String lastName,
                                 @RequestParam(required = false) String middleName,
                                 @RequestParam(required = false) String email,
                                 @RequestParam(required = false) Long groupId,
                                 @RequestParam(required = false, defaultValue = "false") Boolean includeDeleted,
                                 @RequestParam(required = false, defaultValue = "0") Integer page,
                                 @RequestParam(required = false, defaultValue = "10") Integer size,
                                 @RequestParam(required = false, defaultValue = "id") String sortBy,
                                 @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        
        Page<Student> students = studentService.searchStudents(firstName, lastName, middleName, 
                                                               email, groupId, includeDeleted,
                                                               page, size, sortBy, sortDir);
        
        List<Group> groups = groupService.findAll();
        
        model.addAttribute("students", students.getContent());
        model.addAttribute("groups", groups);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", students.getTotalPages());
        model.addAttribute("totalElements", students.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
        // Параметры поиска
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("middleName", middleName);
        model.addAttribute("email", email);
        model.addAttribute("groupId", groupId);
        model.addAttribute("includeDeleted", includeDeleted);
        
        return "students/list";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("groups", groupService.findAll());
        return "students/add";
    }
    
    @PostMapping("/add")
    public String addStudent(@Valid @ModelAttribute Student student, 
                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("groups", groupService.findAll());
            return "students/add";
        }
        
        studentService.save(student);
        return "redirect:/students";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = studentService.findById(id).orElse(null);
        if (student == null) {
            return "redirect:/students";
        }
        
        model.addAttribute("student", student);
        model.addAttribute("groups", groupService.findAll());
        return "students/edit";
    }
    
    @PostMapping("/edit")
    public String updateStudent(@Valid @ModelAttribute Student student, 
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("groups", groupService.findAll());
            return "students/edit";
        }
        
        studentService.save(student);
        return "redirect:/students";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteById(id);
        return "redirect:/students";
    }
    
    @PostMapping("/soft-delete/{id}")
    public String softDeleteStudent(@PathVariable Long id) {
        studentService.softDelete(id);
        return "redirect:/students";
    }
    
    @PostMapping("/restore/{id}")
    public String restoreStudent(@PathVariable Long id) {
        studentService.restore(id);
        return "redirect:/students";
    }
    
    @PostMapping("/bulk-soft-delete")
    public String bulkSoftDelete(@RequestParam(value = "ids", required = false) List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            studentService.bulkSoftDelete(ids);
        }
        return "redirect:/students";
    }
    
    @PostMapping("/bulk-hard-delete")
    public String bulkHardDelete(@RequestParam(value = "ids", required = false) List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            studentService.bulkHardDelete(ids);
        }
        return "redirect:/students";
    }
    
    @GetMapping("/view/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        Student student = studentService.findById(id).orElse(null);
        if (student == null) {
            return "redirect:/students";
        }
        
        model.addAttribute("student", student);
        return "students/view";
    }
}