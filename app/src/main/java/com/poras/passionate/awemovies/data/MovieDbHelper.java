package com.poras.passionate.awemovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by purus on 1/7/2018.
 */

class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 3;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WEATHER_TABLE =

                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +

                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE, " +

                        MovieContract.MovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL," +

                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +

                        MovieContract.MovieEntry.COLUMN_MOVIE_RATING + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
