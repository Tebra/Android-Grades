package com.example.user.notendings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    // Modulen Tabelle
    public static final String TABLE_NAME_MODULE = "module";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_MODUL_KUERZEL = "modulKuerzel";
    public static final String KEY_MODUL_NAME = "modulName";

    // Noten Tabelle
    public static final String TABLE_NAME_NOTEN = "noten";
    public static final String KEY_NOTEN_ART = "notenArt";
    public static final String KEY_NOTEN_PROZENT = "notenProzent";
    public static final String KEY_NOTEN_NOTE = "notenNote";
    public static final String KEY_SECONDARY = "secondaryKey";

    // Datenbank name
    static String DATABASE_NAME = "userdata";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_MODULE = "CREATE TABLE " + TABLE_NAME_MODULE +
                " ( " + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MODUL_KUERZEL + " TEXT, " + KEY_MODUL_NAME + " TEXT )";
        String CREATE_TABLE_NOTEN = "CREATE TABLE " + TABLE_NAME_NOTEN +
                " ( " + KEY_ROWID + " INTEGER PRIMARY KEY, " + KEY_NOTEN_ART + " TEXT, "
                + KEY_NOTEN_PROZENT + " INTEGER, " + KEY_NOTEN_NOTE + " FLOAT, " + KEY_SECONDARY + " INTEGER )";
        db.execSQL(CREATE_TABLE_MODULE);
        db.execSQL(CREATE_TABLE_NOTEN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MODULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTEN);
        onCreate(db);
    }

    public void createNewModule(ModulModel modul) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MODUL_KUERZEL, modul.getModulKuerzel());
        values.put(KEY_MODUL_NAME, modul.getModulName());

        long result = db.insert(TABLE_NAME_MODULE, null, values);
        /*Log.d("DBhelper", "" + result);
        Log.d("DbPath", db.getPath());
        Log.d("DbPath", modul.getModulKuerzel());
        Log.d("DbPath", modul.getModulName());*/
        db.close();
    }

    public ArrayList<String> getAllModules() {
        ArrayList<String> values = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_MODULE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                values.add(cursor.getString(cursor.getColumnIndex(KEY_MODUL_KUERZEL)));
            } while (cursor.moveToNext());
        }
        //Log.d("DbHelper", values.toString());
        return values;
    }

    public void deleteModule(String KEY_DEL) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Log.d("DBHelper", KEY_DEL);
        String deleteQuery = "DELETE FROM " + TABLE_NAME_MODULE + " WHERE " + KEY_MODUL_KUERZEL + " = '" + KEY_DEL + "'";
        //Log.d("DBHelper", deleteQuery);
        db.execSQL(deleteQuery);
        db.close();
    }

    /**
     * Noten mit dem ROWID vom Modul abfragen \n
     *
     * @param modulKuerzel
     * @return
     */
    public String modulOeffnen(String modulKuerzel) {

        SQLiteDatabase db = this.getReadableDatabase();
        //String searchID = "SELECT KEY_ROWID FROM "+ TABLE_NAME_MODULE + " WHERE " + KEY_MODUL_KUERZEL + " = '" + modulKuerzel + "'";

        //"tablename", new String[]{"col1", "col2", "col1 = ? AND col2 = ?", new String[]{"val1", "val2"}, null, null, null);
        Cursor cursor = db.query(TABLE_NAME_MODULE, new String[]{KEY_ROWID}, "modulKuerzel = ?", new String[]{modulKuerzel}, null, null, null);

        String idLinker = "5";

        if (cursor.moveToFirst())
            idLinker = cursor.getString(cursor.getColumnIndex(KEY_ROWID));


        //Log.d("DbHelper", idLinker);
        db.close();

        return (idLinker);
    }

    public String modulVollName(String idLinker) {
        SQLiteDatabase db = this.getReadableDatabase();

        String vollerName = "";

        Cursor cursor = db.query(TABLE_NAME_MODULE, new String[]{KEY_MODUL_NAME}, "_id = ?", new String[]{idLinker}, null, null, null);
        if (cursor.moveToFirst())
            vollerName = cursor.getString(cursor.getColumnIndex(KEY_MODUL_NAME));

        //Log.d("DbHelper", vollerName);
        db.close();

        return vollerName;
    }

    public void createNewNote(String notenArt, String notenProzent, String notenNote, String linker) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOTEN_ART, notenArt);
        values.put(KEY_NOTEN_PROZENT, notenProzent);
        values.put(KEY_NOTEN_NOTE, notenNote);
        values.put(KEY_SECONDARY, linker);

        long result = db.insert(TABLE_NAME_NOTEN, null, values);
        //Log.d("DBhelper", "" + result);
        db.close();


    }

    public Cursor getAllNoten(String idLinker) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = null;

        if (idLinker != null) {
            mCursor = db.query(TABLE_NAME_NOTEN, new String[]{KEY_ROWID, KEY_NOTEN_ART, KEY_NOTEN_PROZENT, KEY_NOTEN_NOTE}, "secondaryKey = ? ", new String[]{idLinker}, null, null, null);
        }

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public void reset() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_NAME_MODULE);
        db.execSQL("DELETE FROM " + TABLE_NAME_NOTEN);
        db.close();
    }

    public void deleteNote(String modulArt, String idLinker) {
        if (idLinker != null) {
            SQLiteDatabase db = this.getWritableDatabase();

            //Log.d("DBHelper", modulArt);
            String deleteQuery = "DELETE FROM " + TABLE_NAME_NOTEN + " WHERE " + KEY_NOTEN_ART + " = '" + modulArt + "' AND " + KEY_SECONDARY + " = '" + idLinker + "'";
            //Log.d("DBHelper", deleteQuery);
            db.execSQL(deleteQuery);
            db.close();


        }
    }

    public void deleteNoteofModule(String idLinker) {
        SQLiteDatabase db = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM " + TABLE_NAME_NOTEN + " WHERE " + KEY_SECONDARY + " = '" + idLinker + "'";
        db.execSQL(deleteQuery);
        db.close();
    }
}