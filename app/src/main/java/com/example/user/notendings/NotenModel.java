package com.example.user.notendings;

/**
 * Created by user on 02.12.2014.
 */
public class NotenModel {
    String notenArt;
    int prozent;
    float note;

    public NotenModel() {
    }

    public NotenModel(String notenArt, int prozent, float note) {
        this.notenArt = notenArt;
        this.prozent = prozent;
        this.note = note;
    }

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
}
