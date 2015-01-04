package com.olimpiadafdi.quicktest.data;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String question;
    private int id;
    private int correct;
    private int tipo;
    ArrayList<Answer> answers;

    public Question(String question, int id, int correct, int tipo) {
        this.question = question;
        this.id = id;
        this.correct = correct;
        this.tipo = tipo;
        this.answers = new ArrayList<Answer>();
    }

    public void setCorrect(int correct){
        this.correct = correct;
    }

    public String getQuestion() {
        return question;
    }

    public int getId(){
        return id;
    }

    public int getCorrect() {
        return correct;
    }

    public int getTipo(){
        return tipo;
    }

    public List<Answer> getAnswers(){
        return answers;
    }
}
