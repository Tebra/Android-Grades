package com.example.johndoe.notendesktop;

/**
 * Created by John Doe on 30.11.2014.
 */
public class ModulModel {
    String kuerzel;
    String modulName;

    public ModulModel(){}

    public ModulModel(String kuerzel, String modulName){
        this.modulName = modulName;
        this.kuerzel = kuerzel;
    }

    public void setKuerzel(String kuerzel) {
        this.kuerzel = kuerzel;
    }

    public void setModulName(String modulName) {
        this.modulName = modulName;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public String getModulName() {
        return modulName;
    }
}
