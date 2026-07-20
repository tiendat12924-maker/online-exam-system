package com.example.TTGT2_THPT.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "test_attempt")
public class TestAttempts {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 @ManyToOne
	 @JoinColumn(name = "user_id")
	 private User user;
	 @ManyToOne
	 @JoinColumn(name = "test_id")
	 private Test test;
	 private Integer correctCount;
	 private Integer wrongCount;
	 private Double score;
	 private Integer timeSpent;
	 private LocalDateTime startedAt;
	 private LocalDateTime submittedAt;
	 private AttemptStatus status;
	 
	 public Long getId() {return id;}
	 public void setId(Long id) {this.id = id;}
	 
	 public User getUser() {return user;}
	 public void setUser(User user) {this.user = user;}
	 
	 public Test getTest() {return test;}
	 public void setTest(Test test) {this.test = test;}
	 
	 public Integer getCorrectCount() {return correctCount;}
	 public void setCorrectCount(Integer correctCount) {this.correctCount = correctCount;}
	 
	 public Integer getWrongCount() {return wrongCount;}
	 public void setWrongCount(Integer wrongCount) {this.wrongCount = wrongCount;}
	 
	 public Double getScore() {return score;}
	 public void setScore(Double score) {this.score = score;}
	 
	 public Integer getTimeSpent() {return timeSpent;}
	 public void setTimeSpent(Integer timeSpent) {this.timeSpent = timeSpent;}
	 
	 public LocalDateTime getStartedAt() {return startedAt;}
	 public void setStartedAt(LocalDateTime startedAt) {this.startedAt = startedAt;}
	 
	 public LocalDateTime getSubmittedAt() {return submittedAt;}
	 public void setSubmittedAt(LocalDateTime submittedAt) {this.submittedAt = submittedAt;}
	 
	 public AttemptStatus getStatus() {return status;}
	 public void setStatus(AttemptStatus status) {this.status = status;}
	 
	 public TestAttempts(Long id, User user, Test test, Integer correctCount, Integer wrongCount,
			Double score, Integer timeSpent, LocalDateTime startedAt,
			LocalDateTime submittedAt, AttemptStatus status) {
		super();
		this.id = id;
		this.user = user;
		this.test = test;
		this.correctCount = correctCount;
		this.wrongCount = wrongCount;
		this.score = score;
		this.timeSpent = timeSpent;
		this.startedAt = startedAt;
		this.submittedAt = submittedAt;
		this.status = status;
	 }
	 
	 public TestAttempts() {
		super();

	 }
	 	 
}
