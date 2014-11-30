package com.example.johndoe.notendesktop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity {

    final Context context = this;
    ArrayList<String> list = new ArrayList<String>();
    ImageButton newNoteButton;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.theListDesign);
        newNoteButton = (ImageButton) findViewById(R.id.newNoteButton);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "Created by rijad.zuzo@gmail.com", Toast.LENGTH_LONG).show();
        }

        if (id == R.id.new_modul) {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.add_new_module_layout, null);

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder.setCancelable(false).setPositiveButton("Fertig", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Hallo", Toast.LENGTH_SHORT).show();

                    EditText modulKurzel = (EditText) findViewById(R.id.editText1);
                    EditText modulName = (EditText) findViewById(R.id.editText2);

                    String modulKuerzelText = modulKurzel.getText().toString();
                    String modulNameText = modulName.getText().toString();

                    addNewModulName(modulKuerzelText, modulNameText);

                }
            })
                    .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    public void addNewModulName(String modulKuerzel, String modulName) {
        ModulModel modul = new ModulModel(modulKuerzel, modulName);
        DbHelper dbHelper = new DbHelper(context);

        dbHelper.addNewModulDB(modul);
    }


    public void addNewNote(View view) {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.add_noten_im_modul, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder.setCancelable(false).setPositiveButton("Fertig", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(MainActivity.this, "Hallo", Toast.LENGTH_SHORT).show();

            }
        })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
}
