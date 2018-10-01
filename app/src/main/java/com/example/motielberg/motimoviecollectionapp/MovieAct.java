package com.example.motielberg.motimoviecollectionapp;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MovieAct extends Activity {
    private EditText title, rls, over, imagePath;
    private RatingBar ratingBar;
    private ImageView img;
    private MovieDBHelper helper;
    private Movie movie;
    private boolean addUpdate;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        helper = new MovieDBHelper(this);
        title = findViewById(R.id.txtMovieTitle);
        ratingBar = findViewById(R.id.ratingBar);
        rls = findViewById(R.id.txtRlsDt);
        over = findViewById(R.id.txtOver);
        imagePath = findViewById(R.id.txtImgUrl);
        img = findViewById(R.id.img2);
        movie = (Movie) getIntent().getSerializableExtra("movie");
        addUpdate = getIntent().getBooleanExtra("addUpdate", false);
        btn = findViewById(R.id.btnSave);
        btn.setText(R.string.save);
        if (addUpdate != false) {
            btn.setText(R.string.update);
        }
        if (movie != null) {
            title.setText(movie.getTitle());
            rls.setText(movie.getReleaseDate());
            over.setText(movie.getOverview());
            ratingBar.setRating(movie.getMovieRate());
            imagePath.setText(movie.getImagePath());
            try {
                Picasso.get().load(imagePath.getText().toString()).error(R.mipmap.moti).into(img);
            } catch (IllegalArgumentException e) {
                Log.e("Error: ", e.toString());
            }
        }

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title.getText().toString().isEmpty()&&(!imagePath.getText().toString().isEmpty())) {
                    if (movie == null) {
                        //insert new movie
                        helper.insertMovie(new Movie(title.getText().toString(),
                                rls.getText().toString(), over.getText().toString(),
                                imagePath.getText().toString()));
                        finish();
                    } else {
                        //update existing movie
                        movie.setTitle(title.getText().toString());
                        movie.setReleaseDate(rls.getText().toString());
                        movie.setOverview(over.getText().toString());
                        movie.setMovieRate((int) ratingBar.getRating());
                        movie.setImagePath(imagePath.getText().toString());
                        if (addUpdate == false) {
                            helper.insertMovie(movie);
                            finish();
                        } else {
                            helper.updateMovie(movie);
                            finish();
                        }
                    }
                } else {
                    Toast.makeText(MovieAct.this, (getString(R.string.missing_info)), Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.btnShow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imagePath.getText().toString().isEmpty()) {
                    Picasso.get().load(imagePath.getText().toString()).error(R.mipmap.moti).into(img);
                } else {
                    imagePath.setText(R.string.missing_img);
                }
            }
        });
    }
}
