package com.mpt.journal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Table(name = "groups")
@SQLRestriction("deleted = false")
public class Group {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Название группы не может быть пустым")
    @Size(min = 2, max = 50, message = "Название группы должно содержать от 2 до 50 символов")
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    @Column(name = "description")
    private String description;
    
    @Column(name = "`year`")
    private Integer year;
    
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
    
    // Связь One-to-Many с Student
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Student> students;
    
    // Связь Many-to-One с Department
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    
    // Конструкторы
    public Group() {}
    
    public Group(String name, String description, Integer year) {
        this.name = name;
        this.description = description;
        this.year = year;
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
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
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
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", year=" + year +
                '}';
    }
}
