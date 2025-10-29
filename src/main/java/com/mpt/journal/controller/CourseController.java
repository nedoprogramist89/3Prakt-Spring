package com.mpt.journal.controller;

import com.mpt.journal.entity.Course;
import com.mpt.journal.entity.Department;
import com.mpt.journal.service.CourseService;
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
@RequestMapping("/courses")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private DepartmentService departmentService;
    
    @GetMapping
    public String getAllCourses(Model model,
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false) String description,
                               @RequestParam(required = false) Integer credits,
                               @RequestParam(required = false) Integer hours,
                               @RequestParam(required = false) Long departmentId,
                               @RequestParam(required = false, defaultValue = "false") Boolean includeDeleted,
                               @RequestParam(required = false, defaultValue = "0") Integer page,
                               @RequestParam(required = false, defaultValue = "10") Integer size,
                               @RequestParam(required = false, defaultValue = "id") String sortBy,
                               @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        
        Page<Course> courses = courseService.searchCourses(name, description, credits, hours, 
                                                           departmentId, includeDeleted,
                                                           page, size, sortBy, sortDir);
        
        List<Department> departments = departmentService.findAll();
        
        model.addAttribute("courses", courses.getContent());
        model.addAttribute("departments", departments);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", courses.getTotalPages());
        model.addAttribute("totalElements", courses.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
        model.addAttribute("name", name);
        model.addAttribute("description", description);
        model.addAttribute("credits", credits);
        model.addAttribute("hours", hours);
        model.addAttribute("departmentId", departmentId);
        model.addAttribute("includeDeleted", includeDeleted);
        
        return "courses/list";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("departments", departmentService.findAll());
        return "courses/add";
    }
    
    @PostMapping("/add")
    public String addCourse(@Valid @ModelAttribute Course course, 
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            return "courses/add";
        }
        
        courseService.save(course);
        return "redirect:/courses";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id).orElse(null);
        if (course == null) {
            return "redirect:/courses";
        }
        
        model.addAttribute("course", course);
        model.addAttribute("departments", departmentService.findAll());
        return "courses/edit";
    }
    
    @PostMapping("/edit")
    public String updateCourse(@Valid @ModelAttribute Course course, 
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            return "courses/edit";
        }
        
        courseService.save(course);
        return "redirect:/courses";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteById(id);
        return "redirect:/courses";
    }
    
    @PostMapping("/soft-delete/{id}")
    public String softDeleteCourse(@PathVariable Long id) {
        courseService.softDelete(id);
        return "redirect:/courses";
    }
    
    @PostMapping("/restore/{id}")
    public String restoreCourse(@PathVariable Long id) {
        courseService.restore(id);
        return "redirect:/courses";
    }
    
    @PostMapping("/bulk-soft-delete")
    public String bulkSoftDelete(@RequestParam(value = "ids", required = false) List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            courseService.bulkSoftDelete(ids);
        }
        return "redirect:/courses";
    }
    
    @PostMapping("/bulk-hard-delete")
    public String bulkHardDelete(@RequestParam(value = "ids", required = false) List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            courseService.bulkHardDelete(ids);
        }
        return "redirect:/courses";
    }
    
    @GetMapping("/view/{id}")
    public String viewCourse(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id).orElse(null);
        if (course == null) {
            return "redirect:/courses";
        }
        
        model.addAttribute("course", course);
        return "courses/view";
    }
}
