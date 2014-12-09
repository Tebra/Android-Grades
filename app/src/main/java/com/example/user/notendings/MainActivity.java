package com.example.user.notendings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    final Context context = this;
    DbHelper db = new DbHelper(context);

    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    ImageButton newNoteButton;
    ListView listview;
    TextView modulNameTextView;
    ListView notenView;
    TextView minNote;
    TextView endNote;
    SimpleCursorAdapter dataAdapter;
    EditText wantedNote;

    String idLinker;
    float prozentZahl = 0;
    int notenAnzahl = 0;
    float prozentRechner = 0;
    float notenSumme = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newNoteButton = (ImageButton) findViewById(R.id.newNoteButton);
        listview = (ListView) findViewById(R.id.theListDesign);
        notenView = (ListView) findViewById(R.id.notenList);
        modulNameTextView = (TextView) findViewById(R.id.modulName);
        minNote = (TextView) findViewById(R.id.minNote);
        endNote = (TextView) findViewById(R.id.maxNote);
        wantedNote = (EditText) findViewById(R.id.gewunschteNote);

        populateModulenList();

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int positionFinal = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String modulDelete = listview.getItemAtPosition(positionFinal).toString();
                        //Log.d("DBHelper", modulDelete);
                        deleteModuleFromDb(modulDelete);
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
                return true;
            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String modulKuerzel = parent.getItemAtPosition(position).toString();
                idLinker = db.modulOeffnen(modulKuerzel);
                populateMainPanel(idLinker);
            }
        });


        notenView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                //Log.d("Position", String.valueOf(position))

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor c = ((SimpleCursorAdapter) parent.getAdapter()).getCursor();
                        db.deleteNote(c.getString(c.getColumnIndex(db.KEY_NOTEN_ART)), idLinker);

                        populateMainPanel(idLinker);

                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });

        /*wantedNote.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strEnteredVal = wantedNote.getText().toString();

                if (!strEnteredVal.equals("")) {
                    int num = Integer.parseInt(strEnteredVal);
                    if (num <= 6) {
                        wantedNote.setText("" + num);
                    } else {
                        wantedNote.setText("");
                    }
                }
            }
        });*/
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
            final View promptsView = li.inflate(R.layout.add_new_module_layout, null);

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder.setCancelable(false).setPositiveButton("Fertig", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    final EditText modulKuerzel = (EditText) promptsView.findViewById(R.id.editText1);
                    final EditText modulName = (EditText) promptsView.findViewById(R.id.editText2);
                    /**
                     * If EditText contains any text then proceed.
                     */
                    if (!TextUtils.isEmpty(modulKuerzel.getText().toString()) || !TextUtils.isEmpty(modulName.getText().toString())) {
                        String modulKurzText = modulKuerzel.getText().toString();
                        String modulNameText = modulName.getText().toString();

                        createNewModule(modulKurzText, modulNameText);
                        populateModulenList();

                        Toast.makeText(MainActivity.this, modulKurzText + " created", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this, "Fehler, nochmal!", Toast.LENGTH_LONG).show();
                    }
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

        if (id == R.id.reset) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Delete all data?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    db.reset();
                    populateModulenList();

                }
            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addNewNote(View view) {
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.add_noten_im_modul, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder.setCancelable(false).setPositiveButton("Fertig", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final EditText notenArt = (EditText) promptsView.findViewById(R.id.editTextTestName);
                final EditText notenProzent = (EditText) promptsView.findViewById(R.id.editTestNotenProzentEingabe);
                final EditText notenNote = (EditText) promptsView.findViewById(R.id.editTextNotenEingabe);

                if (!TextUtils.isEmpty(notenArt.getText().toString()) || !TextUtils.isEmpty(notenProzent.getText().toString()) || !TextUtils.isEmpty(notenNote.getText().toString()))
                    ;
                {
                    db.createNewNote(notenArt.getText().toString(), notenProzent.getText().toString(), notenNote.getText().toString(), idLinker);
                }

                populateMainPanel(idLinker);

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

    public void createNewModule(String modulKuerzel, String modulName) {
        ModulModel modul = new ModulModel(modulKuerzel, modulName);
        db.createNewModule(modul);
    }

    private void populateModulenList() {
        /*String[] values = new String[]{"AKK-E", "AKK-K", "DWW-M",
                "DWW-W", "SYS-A-M", "IuK-C", "IuK-W", "IuK-S", "IuK-D"};*/

        String[] values = db.getAllModules().toArray(new String[db.getAllModules().size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        listview.setAdapter(adapter);
    }

    private void deleteModuleFromDb(String KEY) {
        db.deleteModule(KEY);
        populateModulenList();
    }

    private void populateMainPanel(String idLinker) {
        modulNameTextView.setText(db.modulVollName(idLinker));

        Cursor cursor = db.getAllNoten(idLinker);

        /**
         * Notenberechnungen
         */

        prozentZahl = 0;
        notenAnzahl = 0;
        prozentRechner = 0;
        notenSumme = 0;
        float restProzent = 0;

        if (cursor.getCount() != 0) {
            if (!cursor.getString(cursor.getColumnIndex(db.KEY_NOTEN_PROZENT)).equals("") || !cursor.getString(cursor.getColumnIndex(db.KEY_NOTEN_NOTE)).equals("")) {
                do {
                    prozentRechner = Integer.parseInt(cursor.getString(cursor.getColumnIndex(db.KEY_NOTEN_PROZENT)));
                    prozentZahl += Integer.parseInt(cursor.getString(cursor.getColumnIndex(db.KEY_NOTEN_PROZENT)));
                    //Log.d("Prozente", "" + prozentZahl);

                    notenAnzahl++;

                    float ausgabe = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_NOTEN_NOTE)));
                    //Log.d("ausgabe", "" + ausgabe);
                    notenSumme += (prozentRechner / 100) * (Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_NOTEN_NOTE))));
                    //Log.d("Check", "" + notenSumme);
                }
                while (cursor.moveToNext());
            }
        }

        if (prozentZahl == 100) {
            minNote.setText("//");
            endNote.setText("" + notenSumme);

        } else if (prozentZahl > 100) {
            minNote.setText("Fehler");
            endNote.setText("Fehler");

        } else if (prozentZahl == 0) {
            minNote.setText("//");
            endNote.setText("//");

        } else {
            restProzent = 100 - prozentZahl;
            float mindestNote = (float) (3.75 - notenSumme) / (restProzent / 100);
            //Log.d("vorRounden", "" + mindestNote);
            mindestNote = (float) Math.round(mindestNote * 100) / 100;

            if (mindestNote > 6) {
                minNote.setText("6.0");
                endNote.setText("//");
            } else {
                minNote.setText("" + mindestNote);
                endNote.setText("//");
            }
        }

        cursor.moveToFirst();

        String from[] = new String[]{db.KEY_NOTEN_ART, db.KEY_NOTEN_PROZENT, db.KEY_NOTEN_NOTE};
        int to[] = new int[]{R.id.modulName, R.id.zwischenP, R.id.note};

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.noten_view,
                cursor,
                from,
                to,
                0);

        notenView.setAdapter(dataAdapter);
        db.close();
    }

    public void setWantedGrade(View view) {

        if (prozentZahl < 100) {
            float wunschNote = 0;
            findViewById(R.id.mainLayout).requestFocus();
            if (!wantedNote.getText().toString().equals("")) {
                wunschNote = Float.parseFloat(wantedNote.getText().toString());
            }

            float restProzent = 100 - prozentZahl;

            //Log.d("Werte", prozentZahl + " / " + restProzent + " / " + notenSumme + " / " + wunschNote);

            if (wunschNote != 0) {
                float mindestNote = (float) (wunschNote - notenSumme) / (restProzent / 100);
                mindestNote = (float) Math.round(mindestNote * 100) / 100;

                if (mindestNote <= 6) {
                    minNote.setText("" + mindestNote);
                } else {
                    minNote.setText(">6");
                }

            } else {
                minNote.setText("Fehler");
            }
        } else {
            minNote.setText("//");
            endNote.setText("//");
        }
    }
}
