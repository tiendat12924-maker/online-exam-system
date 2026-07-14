package com.example.TTGT2_THPT.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "answers")
public class Answers {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "label", length = 1, nullable = false)
    private String label;	
	
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;

    @Column(name = "answer_order")
    private Integer answerOrder;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Questions question;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public String getContent() {return content;}
	public void setContent(String content) {this.content = content;}

	public Boolean getIsCorrect() {return isCorrect;}
	public void setIsCorrect(Boolean isCorrect) {this.isCorrect = isCorrect;}

	public Integer getAnswerOrder() {return answerOrder;}
	public void setAnswerOrder(Integer answerOrder) {this.answerOrder = answerOrder;}

	public Questions getQuestion() {return question;}
	public void setQuestion(Questions question) {this.question = question;}
	
	public String getLabel() {return label;}
	public void setLabel(String label) {this.label = label;}
	
	public Answers() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Answers(Long id, String label, String content, Boolean isCorrect, Integer answerOrder, Questions question) {
		super();
		this.id = id;
		this.label = label;
		this.content = content;
		this.isCorrect = isCorrect;
		this.answerOrder = answerOrder;
		this.question = question;
	}
	
	
	
	
    
}
