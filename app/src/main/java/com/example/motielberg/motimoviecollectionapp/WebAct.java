package com.example.motielberg.motimoviecollectionapp;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//import dmax.dialog.SpotsDialog;

public class WebAct extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private WebAdapter adapter;
    private SearchView txtSearch;
    private Button search;
    private ListView listView;
    private String imageUrl = "https://image.tmdb.org/t/p/w500/";
    //    private ProgressDialog progressDialog;
    private String src;
    private ArrayList<Movie> movieArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        //make new array adapter for "adapter" with layout: ***
        adapter = new WebAdapter(this, R.layout.web_movie_list);
        //new list view named "listView" - connected to id of list-on screen
        listView = findViewById(R.id.webList);
        //setting the listView to use adapter
        listView.setAdapter(adapter);
        // connecting search field to Id
        txtSearch = findViewById(R.id.srcField);
        //connecting search btn to click
        search = findViewById(R.id.btnSearch);
        search.setOnClickListener(this);
        listView.setOnItemClickListener(this);

        // savedInstanceState for saving the search result list if phone is flipped
        // if first time open app =savedInstanceState is null - don't load
        //

        if (savedInstanceState != null) {
            movieArrayList = (ArrayList) (savedInstanceState.getSerializable("movieArray"));
            adapter.addAll(movieArrayList);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // method for saving movieArrayList as serializable
        outState.putSerializable("movieArray", movieArrayList);
    }

    @Override
    public void onClick(View v) {
        src = txtSearch.getQuery().toString();
        adapter.clear();
        new PostTask().execute(src);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent gotoEdit = new Intent(WebAct.this, MovieAct.class);
        gotoEdit.putExtra("movie", adapter.getItem(position));
        startActivity(gotoEdit);
    }


    public class PostTask extends AsyncTask<String, Void, ArrayList<Movie>> {
//        SpotsDialog dot;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // progress dialog with moving spots, downloaded from Github.com
            // gets custom style defined under >values>styles.xml
            // gets pre defined string
//            dot = (SpotsDialog) new SpotsDialog.Builder()
//                    .setContext(WebAct.this)
//                    .setTheme(R.style.Custom)
//                    .setMessage(R.string.wait)
//                    .build();
//            dot.show();
        }
        // ** older version progress dialog... cancelled:
        // progressDialog = new ProgressDialog(WebAct.this,ProgressDialog.THEME_HOLO_DARK);
        // progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // progressDialog.setTitle(R.string.wait);
        // progressDialog.setMessage((getString(R.string.suck)));
        // progressDialog.show();

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {
            // try {
            // Thread.sleep(3000);
            // } catch (InterruptedException e) {
            // }
            //declare Https empty connection "connection"
            HttpURLConnection connection = null;
            //declare BufferedReader "reader"
            BufferedReader reader;
            // make a new string builder "builder"
            StringBuilder builder = new StringBuilder();

            //checking for errors: network IO err "IOException", JSON error "JSONException".
            // if no connection - returns "null" to onPostExecute
            // onPostExecute - print error message if any errors or add "result" to "adapter"
            try {
                // new "url" of URL = json link
                URL url = new URL("http://api.themoviedb.org/3/search/movie?api_key=fe327baa8fcca662302742617c10f1d3&query=" + strings[0]);
                // HttpURLConnection "connection" = casting of HttpURLConnection for "url" .openConnection()
                connection = (HttpURLConnection) url.openConnection();
                // if connection get response code which is different than HTTP_OK -return null
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }
                // if  HTTP_OK - make new BufferedReader "reader"
                // "reader" get new InputStreamReader from connection() .getInputStream
                // streaming input to reader
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                // declare string "line" get's reader.readLine();
                String line = reader.readLine();

                // while the line isn't empty add(append) the builder by line(s)
                // line gets next reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
                // when next line is empty:
                // declare "movies" as new empty array list of strings
                ArrayList<Movie> movies = new ArrayList<>();

                // declare "rootArray" as new JSONArray that gets builder.toString();
                // loop count 0 to <rootArray.length, +1 increments
                // every count run:
                JSONObject jsonObject = new JSONObject(builder.toString());
                JSONArray rootArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < rootArray.length(); i++) {
                    // find the Object inside the Array
                    JSONObject postObj = rootArray.getJSONObject(i);
                    // get the info into string for each item
                    String title = postObj.getString("title");
                    String releaseDate = postObj.getString("release_date");
                    String over = postObj.getString("overview");
                    String imagePath = imageUrl + postObj.getString("poster_path");
                    // add the title to the ArrayList
                    movies.add(new Movie(title, releaseDate, over, imagePath));
                }
                // return arrayList of titles
                return movies;

                // if IO error store to e and...
            } catch (java.io.IOException e) {
                // printStackTrace e
                e.printStackTrace();
                // if JSONException error store to e and...
            } catch (JSONException e) {
                // log to e... method "PostsTask" get bad json files. +txt
                Log.e("PostsTask", e.getMessage() + "\nError with JSON");
                // finally if connection is not empty =disconnect. if connection is empty - return null
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            // returns null to onPostExecute if no results from json
            return null;
        }

        // onPostExecute - print error message if any errors or add "result" to "adapter"
        // gets array list of strings and create "result"
        // if "result" is empty - show error message
        // if result has info - add all to adapter
        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            super.onPostExecute(result);
            // after retrieving json data dismiss progressDialog
//            dot.dismiss();
            // if no result from json - make toast: error
            if (result == null) {
                Toast.makeText(WebAct.this, (getString(R.string.error)), Toast.LENGTH_SHORT).show();
                // if getting results add results to adapter (make list)
            } else {
                adapter.addAll(result);
                // lowers keyboard after getting search results
                InputMethodManager lowKeyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                lowKeyboard.hideSoftInputFromWindow(search.getWindowToken(), 0);
                // savedInstanceState for keeping the movie list filled with search result - even after flipping screen
                movieArrayList = result;
            }
        }
    }
}