package com.poras.passionate.awemovies.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;

import com.poras.passionate.awemovies.R;
import com.poras.passionate.awemovies.data.MovieContract;
import com.poras.passionate.awemovies.data.model.Movie;
import com.poras.passionate.awemovies.data.model.Review;
import com.poras.passionate.awemovies.data.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by purus on 1/7/2018.
 */

public class MovieUtil {

    public static final String POPULAR_KEY = "popular";
    public static final String TOP_RATED_KEY = "top_rated";
    public static final String FAVORITE_KEY = "favorite";
    public static final String VIDEOS_KEY = "videos";
    public static final String REVIEWS_KEY = "reviews";

    public static final String SORT_KEY = "sort";
    public static final String MOVIES = "movies";

    private static final String mResults = "results";
    private static final String mPosterPath = "poster_path";
    private static final String mOriginalTitle = "original_title";
    private static final String mReleaseDate = "release_date";
    private static final String mUserRating = "vote_average";
    private static final String mReview = "overview";
    private static final String mId = "id";

    private static final String mLanguageCode = "iso_639_1";
    private static final String mLanguageRegion = "iso_3166_1";
    private static final String mKey = "key";
    private static final String mName = "name";
    private static final String mSite = "site";
    private static final String mSize = "size";
    private static final String mType = "type";


    private static final String mAuthor = "author";
    private static final String mComment = "content";
    private static final String mUrl = "url";

    public static ArrayList<Movie> getMovieData(String response) throws JSONException {
        ArrayList<Movie> movieList = new ArrayList<>();
        JSONArray array;
        JSONObject mObject;
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.has(mResults)) {
            array = jsonObject.getJSONArray(mResults);
            for (int i = 0; i < array.length(); i++) {
                mObject = array.getJSONObject(i);
                movieList.add(new Movie(
                        mObject.getInt(mId),
                        mObject.getString(mPosterPath),
                        mObject.getString(mOriginalTitle),
                        mObject.getString(mReleaseDate),
                        mObject.getDouble(mUserRating),
                        mObject.getString(mReview),
                        false));
            }
        }
        return movieList;
    }

    public static ArrayList<Trailer> getTrailerData(String response) throws JSONException {
        ArrayList<Trailer> trailerList = new ArrayList<>();
        JSONArray array;
        JSONObject mObject;
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.has(mResults)) {
            array = jsonObject.getJSONArray(mResults);
            for (int i = 0; i < array.length(); i++) {
                mObject = array.getJSONObject(i);
                trailerList.add(new Trailer(
                        mObject.getString(mId),
                        mObject.getString(mLanguageCode),
                        mObject.getString(mLanguageRegion),
                        mObject.getString(mKey),
                        mObject.getString(mName),
                        mObject.getString(mSite),
                        mObject.getInt(mSize),
                        mObject.getString(mType)));
            }
        }
        return trailerList;
    }

    public static ArrayList<Review> getReviewData(String response) throws JSONException {
        ArrayList<Review> reviewList = new ArrayList<>();
        JSONArray array;
        JSONObject mObject;
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.has(mResults)) {
            array = jsonObject.getJSONArray(mResults);
            for (int i = 0; i < array.length(); i++) {
                mObject = array.getJSONObject(i);
                reviewList.add(new Review(
                        mObject.getString(mId),
                        mObject.getString(mAuthor),
                        mObject.getString(mComment),
                        mObject.getString(mUrl)));
            }
        }
        return reviewList;
    }

    private static ArrayList<Movie> getMovieListFromCursor(Cursor cursor) {
        ArrayList<Movie> list = new ArrayList<>();
        if (cursor != null) {
            int columnId = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            int columnName = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_NAME);
            int columnPoster = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER);
            int columnOverView = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW);
            int columnRating = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING);
            int columnDate = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);

            while (cursor.moveToNext()) {
                list.add(new Movie(cursor.getInt(columnId),
                        cursor.getString(columnPoster),
                        cursor.getString(columnName),
                        cursor.getString(columnDate),
                        cursor.getDouble(columnRating),
                        cursor.getString(columnOverView),
                        true));
            }
        }
        return list;
    }

    public static ArrayList<Movie> getFavoriteMovieList(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (cursor != null)
            return getMovieListFromCursor(cursor);
        else return null;
    }


    public static void showAlertDialog(Context context) {
        (new AlertDialog.Builder(context).setTitle(context.getString(R.string.alert_title)).setMessage("Already Added as Favorite and is listed in the Favorites!")
                .setNegativeButton(context.getString(R.string.ok_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create()).show();
    }

}
