package com.example.user.notendings;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by user on 02.12.2014.
 */
/*
    public int getProzent() {
        return prozent;
    }

    public void setProzent(int prozent) {
        this.prozent = prozent;
    }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public String getNotenArt() {
        return notenArt;
    }

    public void setNotenArt(String notenArt) {
        this.notenArt = notenArt;
    }

    */

@DatabaseTable(tableName = "grades")
public class Grade {

    @DatabaseField(id = true)
    private String gradeType;
    @DatabaseField
    private int percent;
    @DatabaseField
    private float grade;

    public Grade() {
        // ORMLite needs a no-arg constructor
    }

    public Grade(String gradeType, int percent, float grade) {
        this.gradeType = gradeType;
        this.percent = percent;
        this.grade = grade;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }
}