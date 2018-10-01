package com.example.motielberg.motimoviecollectionapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "movies";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_RLS = "rls";
    private static final String COL_OVER = "over";
    private static final String COL_RATE ="rate";
    private static final String COL_IMG = "img";

    public MovieDBHelper(Context context) {
        super(context, "MovieDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s(%s integer primary key autoincrement, %s text, %s text, %s text, %s integer, %s text)",
                TABLE_NAME, COL_ID, COL_TITLE,COL_RLS, COL_OVER,COL_RATE, COL_IMG);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertMovie(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, movie.getTitle());
        values.put(COL_RLS, movie.getReleaseDate());
        values.put(COL_OVER, movie.getOverview());
        values.put(COL_RATE,movie.getMovieRate());
        values.put(COL_IMG, movie.getImagePath());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateMovie(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, movie.getTitle());
        values.put(COL_RLS, movie.getReleaseDate());
        values.put(COL_OVER, movie.getOverview());
        values.put(COL_RATE,movie.getMovieRate());
        values.put(COL_IMG, movie.getImagePath());

        db.update(TABLE_NAME, values, COL_ID + "=" + movie.getId(), null);
        db.close();
    }

    public void deleteMovie(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + "=" + id, null);
        db.close();
    }

    public void deleteAllMovies() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null,null);
        db.close();
    }

    public ArrayList<Movie> getMovies() {
        ArrayList<Movie> movieArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
            String rls = cursor.getString(cursor.getColumnIndex(COL_RLS));
            String over = cursor.getString(cursor.getColumnIndex(COL_OVER));
            int rate=cursor.getInt(cursor.getColumnIndex(COL_RATE));
            String img = cursor.getString(cursor.getColumnIndex(COL_IMG));

            movieArrayList.add(new Movie(id, title,rls, over,rate, img));
        }
        db.close();
        return movieArrayList;
    }
}
