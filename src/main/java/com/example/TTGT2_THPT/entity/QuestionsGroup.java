package com.example.TTGT2_THPT.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "questions-group")
public class QuestionsGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	@Column(columnDefinition = "TEXT")
    private String instruction;

    @Column(columnDefinition = "TEXT")
    private String passage;

    @Column(name = "test_id")
    private Long testId;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Questions> questions;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}

	public String getInstruction() {return instruction;}
	public void setInstruction(String instruction) {this.instruction = instruction;}

	public String getPassage() {return passage;}
	public void setPassage(String passage) {this.passage = passage;}

	public Long getTestId() {return testId;}
	public void setTestId(Long testId) {this.testId = testId;}

	public List<Questions> getQuestions() {return questions;}
	public void setQuestions(List<Questions> questions) {this.questions = questions;}
	
	public QuestionsGroup() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public QuestionsGroup(Long id, String title, String instruction, String passage, Long testId,
			List<Questions> questions) {
		super();
		this.id = id;
		this.title = title;
		this.instruction = instruction;
		this.passage = passage;
		this.testId = testId;
		this.questions = questions;
	}
    
    
}
