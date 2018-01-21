package com.poras.passionate.awemovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by purus on 1/7/2018.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.poras.passionate.awemovies";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";


    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().
                appendPath(PATH_MOVIES).
                build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_RATING = "rating";
        public static final String COLUMN_MOVIE_NAME = "name";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";


        public static Uri buildMovieUriWithId(int movieId) {
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(movieId)).build();
        }

    }
}
