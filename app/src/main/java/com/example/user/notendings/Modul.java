package com.example.user.notendings;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Rijad Zuzo on 02.12.2014.
 */


@DatabaseTable(tableName = "modules")
public class Modul {

    @DatabaseField(id = true)
    private String name;
    @DatabaseField
    private String kuerzel;

    public Modul() {
        // ORMLite needs a no-arg constructor
    }

    public Modul(String name, String kuerzel) {
        this.name = name;
        this.kuerzel = kuerzel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public void setKuerzel(String password) {
        this.kuerzel = password;
    }
}
