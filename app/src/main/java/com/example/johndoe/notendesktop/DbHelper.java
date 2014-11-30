package com.example.johndoe.notendesktop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    // Modulen Tabelle
    public static final String TABLE_NAME_MODULEN = "module";
    public static final String KEY_MODUL_KURZEL = "modulKurzel";
    public static final String KEY_MODUL_NAME = "modulName";
    public static final String KEY_ROWID = "_id";

    //Noten Tabelle
    public static final String TABLE_NAME_NOTEN = "noten"; /* Modul und noten ID werden gleich sein */
    public static final String KEY_NOTEN_NAME = "notenName";
    public static final String KEY_NOTEN_PROZENT = "notenProzent";
    public static final String KEY_NOTEN_NOTE = "notenNote";

    //Datenbank name
    static String DATABASE_NAME = "userdata";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_MODULEN = "CREATE TABLE " + TABLE_NAME_MODULEN + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MODUL_KURZEL + " TEXT, " + KEY_MODUL_NAME + " TEXT)";
        String CREATE_TABLE_NOTEN = "CREATE TABLE " + TABLE_NAME_NOTEN + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NOTEN_NAME + " TEXT, " + KEY_NOTEN_PROZENT + " INTEGER, "+ KEY_NOTEN_NOTE +" FLOAT)";
        db.execSQL(CREATE_TABLE_MODULEN);
        db.execSQL(CREATE_TABLE_NOTEN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MODULEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTEN);
        onCreate(db);
    }


    public void addNewModulDB(ModulModel modul) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MODUL_KURZEL, modul.getKuerzel());
        values.put(KEY_MODUL_NAME, modul.getModulName());

        db.insert(TABLE_NAME_MODULEN, null, values);
        db.close();
    }

    public ModulModel getAlLModulNames(){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME_MODULEN, )

    }

    public void addNewNoteInModul(NotenModel note){

    }
}