package com.olimpiadafdi.quicktest.data;

public class Answer {
    private String answer;
    private int id;

    public Answer(String answer, int id) {
        this.answer = answer;
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public int getId() {
        return id;
    }
}
