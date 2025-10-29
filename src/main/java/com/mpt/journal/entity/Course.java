package com.mpt.journal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Table(name = "courses")
@SQLRestriction("deleted = false")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Название курса не может быть пустым")
    @Size(min = 2, max = 100, message = "Название курса должно содержать от 2 до 100 символов")
    @Column(name = "name", nullable = false)
    private String name;
    
    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "credits")
    private Integer credits;
    
    @Column(name = "hours")
    private Integer hours;
    
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
    
    // Связь Many-to-Many с Student
    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private List<Student> students;
    
    // Связь Many-to-One с Department
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    
    // Конструкторы
    public Course() {}
    
    public Course(String name, String description, Integer credits, Integer hours) {
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.hours = hours;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getCredits() {
        return credits;
    }
    
    public void setCredits(Integer credits) {
        this.credits = credits;
    }
    
    public Integer getHours() {
        return hours;
    }
    
    public void setHours(Integer hours) {
        this.hours = hours;
    }
    
    public Boolean getDeleted() {
        return deleted;
    }
    
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    
    public List<Student> getStudents() {
        return students;
    }
    
    public void setStudents(List<Student> students) {
        this.students = students;
    }
    
    public Department getDepartment() {
        return department;
    }
    
    public void setDepartment(Department department) {
        this.department = department;
    }
    
    // Методы для логического удаления
    public void softDelete() {
        this.deleted = true;
    }
    
    public void restore() {
        this.deleted = false;
    }
    
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", credits=" + credits +
                ", hours=" + hours +
                '}';
    }
}
