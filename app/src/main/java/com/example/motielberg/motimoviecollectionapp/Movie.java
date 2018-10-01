package com.example.motielberg.motimoviecollectionapp;

import java.io.Serializable;

public class Movie implements Serializable {

    private long id;
    private String title,releaseDate, overview, imagePath;
    private int movieRate;

    public int getMovieRate() {
        return movieRate;
    }

    public void setMovieRate(int movieRate) {
        this.movieRate = movieRate;
    }

    public Movie(String title, String releaseDate, String overview, String imagePath) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.imagePath = imagePath;
    }

    public Movie(long id, String title, String releaseDate, String overview,int movieRate, String imagePath) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.movieRate= movieRate;
        this.imagePath = imagePath;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return id + ": " + title;
    }
}