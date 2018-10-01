package com.example.motielberg.motimoviecollectionapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainAdapter extends ArrayAdapter<Movie> {
    private ImageView imageView;
    private RatingBar ratingBar;
    private TextView movieName, movieReleaseDate;

    public MainAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    //override this method to tell the adapter what to show for each item!
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_list_item, parent, false);
        }
        imageView = convertView.findViewById(R.id.imageView);
        movieName = convertView.findViewById(R.id.txtMovieTitle);
        movieReleaseDate = convertView.findViewById(R.id.txtRlsDate);
        ratingBar = convertView.findViewById(R.id.ratingBar);
        Movie movie = getItem(position);
        Picasso.get().load(movie.getImagePath()).error(R.mipmap.moti).into(imageView);
        movieName.setText(movie.getTitle());
        ratingBar.setRating(movie.getMovieRate());
        movieReleaseDate.setText(movie.getReleaseDate());

        return convertView;
    }
}