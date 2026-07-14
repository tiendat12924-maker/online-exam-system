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
@Table(name = "questions")
public class Questions {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private QuestionsType questionType;
    
    @Column(name = "question_order")
    private Integer questionOrder;
    
    @ManyToOne
    @JoinColumn(name = "group_id")
    private QuestionsGroup group;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answers> answers;
    
    
	public QuestionsType getQuestionType() {return questionType;}
	public void setQuestionType(QuestionsType questionType) {this.questionType = questionType;}
	
	public List<Answers> getAnswers() {return answers;}
	public void setAnswers(List<Answers> answers) {this.answers = answers;}
	
	public Integer getId() {return id;}
	public void setId(Integer id) {this.id = id;}
	
	public String getContent() {return content;}
	public void setContent(String content) {this.content = content;}
	
	public Integer getQuestionOrder() {return questionOrder;}
	public void setQuestionOrder(Integer questionOrder) {this.questionOrder = questionOrder;}
	
	public Test getTest() {return test;}
	public void setTest(Test test) {this.test = test;}
	
	public LocalDateTime getCreatedAt() {return createdAt;}
	public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
	
	public LocalDateTime getUpdatedAt() {return updatedAt;}
	public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}
	
	
	public QuestionsGroup getGroup() {return group;}
	public void setGroup(QuestionsGroup group) {this.group = group;}
	
	
	public Questions(Integer id, String content, QuestionsType questionType, Integer questionOrder,
			QuestionsGroup group, Test test, LocalDateTime createdAt, LocalDateTime updatedAt, List<Answers> answers) {
		super();
		this.id = id;
		this.content = content;
		this.questionType = questionType;
		this.questionOrder = questionOrder;
		this.group = group;
		this.test = test;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.answers = answers;
	}
	public Questions() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	
    
}
