package com.mpt.journal.controller;

import com.mpt.journal.entity.StudentProfile;
import com.mpt.journal.service.StudentProfileService;
import com.mpt.journal.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/profiles")
public class StudentProfileController {
    
    @Autowired
    private StudentProfileService profileService;
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    public String getAllProfiles(Model model,
                                @RequestParam(required = false) String biography,
                                @RequestParam(required = false) String hobbies,
                                @RequestParam(required = false) String achievements,
                                @RequestParam(required = false, defaultValue = "false") Boolean includeDeleted,
                                @RequestParam(required = false, defaultValue = "0") Integer page,
                                @RequestParam(required = false, defaultValue = "10") Integer size,
                                @RequestParam(required = false, defaultValue = "id") String sortBy,
                                @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        
        Page<StudentProfile> profiles = profileService.searchProfiles(biography, hobbies, achievements, 
                                                                      includeDeleted,
                                                                      page, size, sortBy, sortDir);
        
        model.addAttribute("profiles", profiles.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", profiles.getTotalPages());
        model.addAttribute("totalElements", profiles.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
        // Параметры поиска
        model.addAttribute("biography", biography);
        model.addAttribute("hobbies", hobbies);
        model.addAttribute("achievements", achievements);
        model.addAttribute("includeDeleted", includeDeleted);
        
        return "profiles/list";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("profile", new StudentProfile());
        model.addAttribute("students", studentService.findAll());
        return "profiles/add";
    }
    
    @PostMapping("/add")
    public String addProfile(@Valid @ModelAttribute StudentProfile profile, 
                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("students", studentService.findAll());
            return "profiles/add";
        }
        
        profileService.save(profile);
        return "redirect:/profiles";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        StudentProfile profile = profileService.findById(id).orElse(null);
        if (profile == null) {
            return "redirect:/profiles";
        }
        
        model.addAttribute("profile", profile);
        model.addAttribute("students", studentService.findAll());
        return "profiles/edit";
    }
    
    @PostMapping("/edit")
    public String updateProfile(@Valid @ModelAttribute StudentProfile profile, 
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("students", studentService.findAll());
            return "profiles/edit";
        }
        
        profileService.save(profile);
        return "redirect:/profiles";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteProfile(@PathVariable Long id) {
        profileService.deleteById(id);
        return "redirect:/profiles";
    }
    
    @PostMapping("/soft-delete/{id}")
    public String softDeleteProfile(@PathVariable Long id) {
        profileService.softDelete(id);
        return "redirect:/profiles";
    }
    
    @PostMapping("/restore/{id}")
    public String restoreProfile(@PathVariable Long id) {
        profileService.restore(id);
        return "redirect:/profiles";
    }
    
    @PostMapping("/bulk-soft-delete")
    public String bulkSoftDelete(@RequestParam(value = "ids", required = false) List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            profileService.bulkSoftDelete(ids);
        }
        return "redirect:/profiles";
    }
    
    @PostMapping("/bulk-hard-delete")
    public String bulkHardDelete(@RequestParam(value = "ids", required = false) List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            profileService.bulkHardDelete(ids);
        }
        return "redirect:/profiles";
    }
    
    @GetMapping("/view/{id}")
    public String viewProfile(@PathVariable Long id, Model model) {
        StudentProfile profile = profileService.findById(id).orElse(null);
        if (profile == null) {
            return "redirect:/profiles";
        }
        
        model.addAttribute("profile", profile);
        return "profiles/view";
    }
}
