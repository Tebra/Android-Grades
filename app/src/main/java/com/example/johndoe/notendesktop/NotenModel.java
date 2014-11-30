package com.example.johndoe.notendesktop;

/**
 * Created by John Doe on 30.11.2014.
 */
public class NotenModel {

    String notenArt;
    int prozent;
    float note;

    public NotenModel(){}

    public NotenModel(String notenArt, int prozent, float note){
        this.notenArt = notenArt;
        this.prozent = prozent;
        this.note = note;
    }

    public void setNotenArt(String notenArt) {
        this.notenArt = notenArt;
    }

    public void setProzent(int prozent) {
        this.prozent = prozent;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public String getNotenArt() {
        return notenArt;
    }

    public int getProzent() {
        return prozent;
    }

    public float getNote() {
        return note;
    }
}
