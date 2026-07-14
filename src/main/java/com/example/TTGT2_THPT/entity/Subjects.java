package com.example.TTGT2_THPT.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "subjects")
public class Subjects {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;
    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;
    @Column(name = "status")
    private Boolean status;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	
	public Boolean getStatus() {return status;}
	public void setStatus(Boolean status) {this.status = status;}
	
	public LocalDateTime getCreatedAt() {return createdAt;}
	public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
	
	public LocalDateTime getUpdatedAt() {return updatedAt;}
	public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}
	
	public Subjects(Long id, String name, String code, String description, Boolean status, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.description = description;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	public Subjects() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public String getIcon() {
        if (code == null) 
        return "fa-solid fa-book";

        switch (code) {
            case "CHEMISTRY":
                return "fa-solid fa-vial";
            case "PHYSICS":
                return "fa-solid fa-circle-radiation";
            case "MATH":
                return "fa-solid fa-calculator";
            case "ENGLISH":
                return "fa-solid fa-spell-check";
            case "LITERATURE":
                return "fa-solid fa-book";
            case "GEOGRAPHY":
                return "fa-solid fa-earth-oceania";
            case "HISTORY":
                return "fa-solid fa-file-pen";
            case "BIOLOGY":
                return "fa-solid fa-dna";
            default:
                return "fa-solid fa-file";
        }
    }
	
    
}
