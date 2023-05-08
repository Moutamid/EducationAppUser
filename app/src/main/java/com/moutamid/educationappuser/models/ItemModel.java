package com.moutamid.educationappuser.models;

public class ItemModel {
    int icon;
    ClassModel classModel;
    SubjectModel subjectModel;

    public ItemModel(int icon, ClassModel classModel) {
        this.icon = icon;
        this.classModel = classModel;
    }

    public ItemModel(int icon, SubjectModel subjectModel) {
        this.icon = icon;
        this.subjectModel = subjectModel;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public ClassModel getClassModel() {
        return classModel;
    }

    public void setClassModel(ClassModel classModel) {
        this.classModel = classModel;
    }

    public SubjectModel getSubjectModel() {
        return subjectModel;
    }

    public void setSubjectModel(SubjectModel subjectModel) {
        this.subjectModel = subjectModel;
    }
}
