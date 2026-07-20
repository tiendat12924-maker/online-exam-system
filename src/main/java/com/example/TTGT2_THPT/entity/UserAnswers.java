package com.example.TTGT2_THPT.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_answer")
public class UserAnswers {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "attempt_id")
    private TestAttempts attempt;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Questions question;
    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answers answer;
    private Boolean isCorrect;
    
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	public TestAttempts getAttempt() {return attempt;}
	public void setAttempt(TestAttempts attempt) {this.attempt = attempt;}
	
	public Questions getQuestion() {return question;}
	public void setQuestion(Questions question) {this.question = question;}
	
	public Answers getAnswer() {return answer;}
	public void setAnswer(Answers answer) {this.answer = answer;}
	
	public Boolean getIsCorrect() {return isCorrect;}
	public void setIsCorrect(Boolean isCorrect) {this.isCorrect = isCorrect;}
	
	public UserAnswers(Long id, TestAttempts attempt, Questions question, Answers answer, Boolean isCorrect) {
		super();
		this.id = id;
		this.attempt = attempt;
		this.question = question;
		this.answer = answer;
		this.isCorrect = isCorrect;
	}
	
	public UserAnswers() {
		super();
		// TODO Auto-generated constructor stub
	} 
}
