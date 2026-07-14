package com.example.TTGT2_THPT.entity;

import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tests")
public class Test {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subjects subject;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty = Difficulty.MEDIUM;
    @Column(nullable = true)
    private String image;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private Integer year;
    @Column(nullable = false)
    private Integer duration;
    @Column(name = "total_score", nullable = false)
    private Double totalScore = 10.0;
    @Column(name = "created_by")
    private Integer createdBy;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Questions> questions;
    
	public Integer getId() {return id;}
	public void setId(Integer id) {this.id = id;}
	
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	
	public Subjects getSubject() {return subject;}
	public void setSubject(Subjects subject) {this.subject = subject;}
	
	public Difficulty getDifficulty() {return difficulty;}
	public void setDifficulty(Difficulty difficulty) {this.difficulty = difficulty;}
	
	public Integer getDuration() {return duration;}
	public void setDuration(Integer duration) {this.duration = duration;}
	
	public Integer getCreatedBy() {return createdBy;}
	public void setCreatedBy(Integer createdBy) {this.createdBy = createdBy;}
	
	public LocalDateTime getCreatedAt() {return createdAt;}
	public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
	
	public LocalDateTime getUpdatedAt() {return updatedAt;}
	public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}
	
	public List<Questions> getQuestions() {return questions;}
	public void setQuestions(List<Questions> questions) {this.questions = questions;}
	
	public String getImage() {return image;}
	public void setImage(String image) {this.image = image;}
	
	public String getType() {return type;}
	public void setType(String type) {this.type = type;}
	
	public Integer getYear() {return year;}
	public void setYear(Integer year) {this.year = year;}
	
	public Double getTotalScore() {return totalScore;}
	public void setTotalScore(Double totalScore) {this.totalScore = totalScore;}
	
	public Test() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Test(Integer id, String title, Subjects subject, Difficulty difficulty, String image, String type,
			Integer year, Integer duration, Double totalScore, Integer createdBy, LocalDateTime createdAt,
			LocalDateTime updatedAt, List<Questions> questions) {
		super();
		this.id = id;
		this.title = title;
		this.subject = subject;
		this.difficulty = difficulty;
		this.image = image;
		this.type = type;
		this.year = year;
		this.duration = duration;
		this.totalScore = totalScore;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.questions = questions;
	}
	
	
    
    
}
