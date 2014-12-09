package com.example.user.notendings;

/**
 * Created by user on 02.12.2014.
 */
public class ModulModel {
    String modulKuerzel;
    String modulName;

    public ModulModel(){}

    public ModulModel(String modulKuerzel, String modulName){
        this.modulKuerzel = modulKuerzel;
        this.modulName = modulName;
    }

    public String getModulKuerzel() {
        return modulKuerzel;
    }

    public void setModulKuerzel(String modulKuerzel) {
        this.modulKuerzel = modulKuerzel;
    }

    public String getModulName() {
        return modulName;
    }

    public void setModulName(String modulName) {
        this.modulName = modulName;
    }
}
