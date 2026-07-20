package com.example.TTGT2_THPT.dto;

public class ExcelMappingDTO {
    private String questionContentCol;
    private String passageCol;
    private String instructionCol;
    private String answerACol;
    private String answerBCol;
    private String answerCCol;
    private String answerDCol;
    private String correctAnswerCol;
    
    public String getQuestionContentCol() { return questionContentCol;}
    public void setQuestionContentCol(String questionContentCol) {this.questionContentCol = questionContentCol;}
    
    public String getPassageCol() {return passageCol;}
    public void setPassageCol(String passageCol) {
        this.passageCol = passageCol;
    }

    public String getInstructionCol() {
        return instructionCol;
    }

    public void setInstructionCol(String instructionCol) {
        this.instructionCol = instructionCol;
    }

    public String getAnswerACol() {
        return answerACol;
    }

    public void setAnswerACol(String answerACol) {
        this.answerACol = answerACol;
    }

    public String getAnswerBCol() {
        return answerBCol;
    }

    public void setAnswerBCol(String answerBCol) {
        this.answerBCol = answerBCol;
    }

    public String getAnswerCCol() {
        return answerCCol;
    }

    public void setAnswerCCol(String answerCCol) {
        this.answerCCol = answerCCol;
    }

    public String getAnswerDCol() {
        return answerDCol;
    }

    public void setAnswerDCol(String answerDCol) {
        this.answerDCol = answerDCol;
    }

    public String getCorrectAnswerCol() {
        return correctAnswerCol;
    }

    public void setCorrectAnswerCol(String correctAnswerCol) {
        this.correctAnswerCol = correctAnswerCol;
    }
}
