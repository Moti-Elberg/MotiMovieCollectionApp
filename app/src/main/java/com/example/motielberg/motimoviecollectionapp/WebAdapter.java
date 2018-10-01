package com.example.motielberg.motimoviecollectionapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class WebAdapter extends ArrayAdapter<Movie> {
    private ImageView imageView;
    private TextView movieName,movieReleaseDate;

    public WebAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    //override this method to tell the adapter what to show for each item!
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.web_movie_list, parent, false);
        }
        imageView = convertView.findViewById(R.id.imageView);
        movieName = convertView.findViewById(R.id.txtMovieTitle);
        movieReleaseDate = convertView.findViewById(R.id.txtRlsDate);

        Movie movie = getItem(position);

        Picasso.get().load(movie.getImagePath()).into(imageView);
        movieName.setText(movie.getTitle());
        movieReleaseDate.setText(movie.getReleaseDate());

        return convertView;
    }
}
