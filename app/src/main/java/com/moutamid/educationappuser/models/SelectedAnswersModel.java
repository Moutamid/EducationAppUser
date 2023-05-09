package com.moutamid.educationappuser.models;

public class SelectedAnswersModel {
    String answer;
    int position;

    public SelectedAnswersModel() {
    }

    public SelectedAnswersModel(String answer, int position) {
        this.answer = answer;
        this.position = position;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
