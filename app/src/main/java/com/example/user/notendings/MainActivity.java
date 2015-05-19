package com.example.user.notendings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
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

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MainActivity extends ActionBarActivity {

    final Context context = this;
    DbHelper db = new DbHelper(context);

    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    ImageButton newGradeButton;
    ListView moduleListView;
    TextView modulNameTextView;
    ListView gradesListView;
    TextView minGrade;
    TextView endGrade;
    SimpleCursorAdapter dataAdapter;
    EditText wantedGrade;

    String idLinker = null;
    float prozentZahl = 0;
    float prozentRechner = 0;
    float notenSumme = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        newGradeButton = (ImageButton) findViewById(R.id.newNoteButton);
        moduleListView = (ListView) findViewById(R.id.theListDesign);
        gradesListView = (ListView) findViewById(R.id.notenList);
        modulNameTextView = (TextView) findViewById(R.id.modulName);
        minGrade = (TextView) findViewById(R.id.minNote);
        endGrade = (TextView) findViewById(R.id.maxNote);
        wantedGrade = (EditText) findViewById(R.id.gewunschteNote);

        populateModuleList();

        moduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String modulShortName = parent.getItemAtPosition(position).toString();
                idLinker = db.modulOeffnen(modulShortName);
                populateGradePanel(idLinker);
            }
        });

        moduleListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int positionFinal = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String modulDelete = moduleListView.getItemAtPosition(positionFinal).toString();
                        idLinker = db.modulOeffnen(modulDelete);
                        //Log.d("DBHelper", modulDelete);
                        deleteModuleFromDb(modulDelete);
                        db.deleteNoteofModule(idLinker);
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


        gradesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                //Log.d("Position", String.valueOf(position))

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor c = ((SimpleCursorAdapter) parent.getAdapter()).getCursor();
                        db.deleteNote(c.getString(c.getColumnIndex(db.KEY_NOTEN_ART)), idLinker);

                        populateGradePanel(idLinker);

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
            final LoadToast lodiTo = new LoadToast(context);

            alertDialogBuilder.setCancelable(false).setPositiveButton("Fertig", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final EditText modulKuerzel = (EditText) promptsView.findViewById(R.id.editText1);
                    final EditText modulName = (EditText) promptsView.findViewById(R.id.editText2);
                    /**
                     * If EditText contains any text then proceed.
                     */
                    String modulKurzText = modulKuerzel.getText().toString();
                    String modulNameText = modulName.getText().toString();

                    if ( !modulKurzText.isEmpty() && !modulNameText.isEmpty() ) {
                        createNewModule(modulKurzText, modulNameText);
                        populateModuleList();
                        Toast.makeText(MainActivity.this, modulKurzText + " created", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this, "Fehler, leere Stellen", Toast.LENGTH_LONG).show();
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
                    populateModuleList();

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

    public void addNewGrade(View view) {
        if (idLinker != null) {
            LayoutInflater li = LayoutInflater.from(context);
            final View promptsView = li.inflate(R.layout.add_noten_im_modul, null);

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder.setCancelable(false).setPositiveButton("Fertig", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final EditText gradeType = (EditText) promptsView.findViewById(R.id.editTextTestName);
                    final EditText gradeProzent = (EditText) promptsView.findViewById(R.id.editTestNotenProzentEingabe);
                    final EditText gradeNumber = (EditText) promptsView.findViewById(R.id.editTextNotenEingabe);

                    if (!TextUtils.isEmpty(gradeType.getText().toString()) && !TextUtils.isEmpty(gradeProzent.getText().toString()) && !TextUtils.isEmpty(gradeNumber.getText().toString())) {
                        db.createNewNote(gradeType.getText().toString(), gradeProzent.getText().toString(), gradeNumber.getText().toString(), idLinker);
                        populateGradePanel(idLinker);
                    } else {
                        Toast.makeText(context, "Fehler, leere Stellen", Toast.LENGTH_LONG).show();
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
        } else {
            Toast.makeText(context, "Bitte Modul Wählen!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Trying to use a model to represent the Modules
     *
     * @param modulKuerzel The Shorthand Name of the Module
     * @param modulName    The Full Name of the Module
     */
    public void createNewModule(String modulKuerzel, String modulName) {
        ModulModel modul = new ModulModel(modulKuerzel, modulName);
        db.createNewModule(modul);
    }

    private void populateModuleList() {
        /*String[] values = new String[]{"AKK-E", "AKK-K", "DWW-M",
                "DWW-W", "SYS-A-M", "IuK-C", "IuK-W", "IuK-S", "IuK-D"};*/

        String[] values = db.getAllModules().toArray(new String[db.getAllModules().size()]);
        Arrays.sort(values);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        moduleListView.setAdapter(adapter);

    }

    private void deleteModuleFromDb(String KEY) {
        db.deleteModule(KEY);
        populateModuleList();
    }

    private void populateGradePanel(String idLinker) {
        modulNameTextView.setText(db.modulVollName(idLinker));

        Cursor cursor = db.getAllNoten(idLinker);

        prozentZahl = 0;
        prozentRechner = 0;
        notenSumme = 0;
        float restProzent = 0;

        if (cursor.getCount() != 0) {
            if (!cursor.getString(cursor.getColumnIndex(db.KEY_NOTEN_PROZENT)).equals("") || !cursor.getString(cursor.getColumnIndex(db.KEY_NOTEN_NOTE)).equals("")) {
                do {
                    prozentRechner = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_NOTEN_PROZENT)));
                    prozentZahl += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_NOTEN_PROZENT)));
                    //Log.d("Prozente", "" + prozentZahl);

                    notenSumme += (prozentRechner / 100.0) * (Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_NOTEN_NOTE))));
                    //Log.d("Check", "" + notenSumme);
                }
                while (cursor.moveToNext());
            }
        }

        if (prozentZahl == 100.0) {
            minGrade.setText("//");
            endGrade.setText("" + notenSumme);

        } else if (prozentZahl > 100.0) {
            minGrade.setText("Fehler Prozent");
            endGrade.setText("Fehler Prozent");

        } else if (prozentZahl == 0.0) {
            minGrade.setText("//");
            endGrade.setText("//");

        } else {
            restProzent = (float) (100.0 - prozentZahl);
            float mindestNote = (float) (3.75 - notenSumme) / (restProzent / 100);
            //Log.d("vorRounden", "" + mindestNote);
            mindestNote = (float) Math.round(mindestNote * 100) / 100;

            if (mindestNote > 6.0) {
                minGrade.setText("6.0");
                endGrade.setText("//");
            }

            else if (mindestNote < 1.0)
            {
                minGrade.setText("1");
                endGrade.setText("//");
            }
            else {
                minGrade.setText("" + mindestNote);
                endGrade.setText("//");
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

        gradesListView.setAdapter(dataAdapter);
        db.close();
    }

    public void setWantedGrade(View view) {

        if (prozentZahl < 100.0) {
            float wunschNote = 0;
            findViewById(R.id.mainLayout).requestFocus();
            if (!wantedGrade.getText().toString().equals("")) {
                wunschNote = Float.parseFloat(wantedGrade.getText().toString());
            }

            float restProzent = (float) (100.0 - prozentZahl);

            //Log.d("Werte", prozentZahl + " / " + restProzent + " / " + notenSumme + " / " + wunschNote);

            if (wunschNote != 0) {
                float mindestNote = (float) (wunschNote - notenSumme) / (restProzent / 100);
                mindestNote = (float) Math.round(mindestNote * 100) / 100;

                if (mindestNote <= 6.0) {
                    minGrade.setText("" + mindestNote);
                } else if (mindestNote < 1) {
                    minGrade.setText("1");
                } else {
                    minGrade.setText(">6");
                }

            } else {
                minGrade.setText("Fehler");
            }
        } else {
            minGrade.setText("//");
            endGrade.setText("//");
        }
    }
}
