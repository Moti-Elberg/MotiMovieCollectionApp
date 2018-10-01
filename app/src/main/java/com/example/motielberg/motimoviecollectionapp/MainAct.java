package com.example.motielberg.motimoviecollectionapp;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import static com.example.motielberg.motimoviecollectionapp.R.mipmap.moti_movie_list_icon_round;

public class MainAct extends Activity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private ListView listView;
    private MainAdapter mainAdapter;
    private MovieDBHelper movieDBHelper;
    private boolean addUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // new database builder of movie items
        movieDBHelper = new MovieDBHelper(this);
        // view adapter building each movie in group of views
        mainAdapter = new MainAdapter(this, R.layout.movie_list_item);
        listView = findViewById(R.id.mainList);
        listView.setAdapter(mainAdapter);
        // boolean for deciding either adding new movie or updating existing movie - to prevent doubles when editing
        addUpdate = true;

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainAct.this, R.style.Theme_AppCompat_Dialog).create();
                alertDialog.setTitle(R.string.add_new_movie);
                alertDialog.setMessage(getString(R.string.choose));
                alertDialog.setIcon(moti_movie_list_icon_round);

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, (getString(R.string.web)), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent web = new Intent(MainAct.this, WebAct.class);
                        web.putExtra("addUpdate", addUpdate = true);
                        startActivity(web);
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, (getString(R.string.manual)), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent manual = new Intent(MainAct.this, MovieAct.class);
                        manual.putExtra("addUpdate", addUpdate = false);
                        startActivity(manual);
                    }
                });
//                alertDialog.getWindow().setBackgroundDrawableResource(R.color.grayDialog);
                alertDialog.show();
            }
        });
        // setting listers for the list on click -edit. long click -delete & open dialog box y/n
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
    }

    // clears movie list (adapter) onStart
    // add all movies to adapter from movieDBHelper.class using getMovies method
    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.clear();
        mainAdapter.addAll(movieDBHelper.getMovies());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // top menu for exit and clearing all movies from DB & list
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // switch case for delete all movies from DB & list
        switch (item.getItemId()) {
            // if choose to delete all movies - show warning dialog
            case R.id.menuDel:
                AlertDialog alertDialog = new AlertDialog.Builder(MainAct.this, R.style.Theme_AppCompat_Dialog).create();
                alertDialog.setTitle(R.string.delTitle);
                alertDialog.setMessage(getString(R.string.delMsg));
                alertDialog.setIcon(moti_movie_list_icon_round);
                // cancel delete
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, (getString(R.string.no)), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainAct.this, (getString(R.string.canceled)), Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.dismiss();

                // delete all movies from DB & clear adapter list
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, (getString(R.string.yes)), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        movieDBHelper.deleteAllMovies();
                        mainAdapter.clear();
                    }
                });
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.grayDialog);
                alertDialog.show();
                break;
            // exit app
            case R.id.menuExit:
                finish();
                System.exit(0);
                break;
        }

        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog delMovie = new AlertDialog.Builder(MainAct.this, R.style.Theme_AppCompat_Dialog).create();
        delMovie.setTitle(R.string.delTitle);
        delMovie.setMessage(getString(R.string.delMsg));
        delMovie.setIcon(R.mipmap.moti_movie_list_icon_round);
        delMovie.setButton(AlertDialog.BUTTON_NEGATIVE, (getString(R.string.no)), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainAct.this, (getString(R.string.canceled)), Toast.LENGTH_LONG).show();
            }
        });
        delMovie.dismiss();
        delMovie.setButton(AlertDialog.BUTTON_POSITIVE, (getString(R.string.yes)), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                movieDBHelper.deleteMovie(mainAdapter.getItem(position).getId());
                mainAdapter.remove(mainAdapter.getItem(position));
            }
        });
        delMovie.getWindow().setBackgroundDrawableResource(R.color.grayDialog);
        delMovie.show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent gotoEdit = new Intent(MainAct.this, MovieAct.class);
        gotoEdit.putExtra("movie", mainAdapter.getItem(position));
        gotoEdit.putExtra("addUpdate", addUpdate = true);
        startActivity(gotoEdit);

        Toast update = Toast.makeText(MainAct.this, (getString(R.string.edit)), Toast.LENGTH_LONG);
        update.setGravity(Gravity.TOP, 0, 0);
        update.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast exitApp = Toast.makeText(MainAct.this, (getString(R.string.exitUser)), Toast.LENGTH_LONG);
//        exitApp.setGravity(Gravity.CENTER, 0, 0);
//        exitApp.show();
    }
}