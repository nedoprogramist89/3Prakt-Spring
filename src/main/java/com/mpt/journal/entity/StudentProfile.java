package com.mpt.journal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "student_profiles")
@SQLRestriction("deleted = false")
public class StudentProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Size(max = 500, message = "Биография не должна превышать 500 символов")
    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography;
    
    @Column(name = "hobbies")
    private String hobbies;
    
    @Column(name = "achievements")
    private String achievements;
    
    @Column(name = "emergency_contact")
    private String emergencyContact;
    
    @Column(name = "emergency_phone")
    private String emergencyPhone;
    
    @Column(name = "created_date")
    private LocalDate createdDate;
    
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    public StudentProfile() {}
    
    public StudentProfile(String biography, String hobbies, String achievements, String emergencyContact, String emergencyPhone) {
        this.biography = biography;
        this.hobbies = hobbies;
        this.achievements = achievements;
        this.emergencyContact = emergencyContact;
        this.emergencyPhone = emergencyPhone;
        this.createdDate = LocalDate.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getBiography() {
        return biography;
    }
    
    public void setBiography(String biography) {
        this.biography = biography;
    }
    
    public String getHobbies() {
        return hobbies;
    }
    
    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }
    
    public String getAchievements() {
        return achievements;
    }
    
    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }
    
    public String getEmergencyContact() {
        return emergencyContact;
    }
    
    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
    
    public String getEmergencyPhone() {
        return emergencyPhone;
    }
    
    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }
    
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    
    public Boolean getDeleted() {
        return deleted;
    }
    
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public void softDelete() {
        this.deleted = true;
    }
    
    public void restore() {
        this.deleted = false;
    }
    
    @Override
    public String toString() {
        return "StudentProfile{" +
                "id=" + id +
                ", biography='" + biography + '\'' +
                ", hobbies='" + hobbies + '\'' +
                ", achievements='" + achievements + '\'' +
                '}';
    }
}
