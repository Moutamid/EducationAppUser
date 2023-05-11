package com.moutamid.educationappuser.models;

public class SaveScoreModel {
    String className, subjectName;
    int score, size;
    long timestamp;

    public SaveScoreModel() {
    }

    public SaveScoreModel(String className, String subjectName, int score, int size, long timestamp) {
        this.className = className;
        this.subjectName = subjectName;
        this.score = score;
        this.size = size;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
