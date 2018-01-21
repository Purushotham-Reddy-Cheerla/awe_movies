package com.poras.passionate.awemovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by purus on 1/7/2018.
 */

public class MovieProvider extends ContentProvider {
    private static final int MOVIES_CODE = 21;
    private static final int MOVIES_WITH_ID_CODE = 28;


    private static final UriMatcher mUriMatcher = getUriMatcher();
    private MovieDbHelper mOpenHelper;

    private static UriMatcher getUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String contentAuthority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(contentAuthority, MovieContract.PATH_MOVIES, MOVIES_CODE);
        matcher.addURI(contentAuthority, MovieContract.PATH_MOVIES + "/#", MOVIES_WITH_ID_CODE);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase database = mOpenHelper.getReadableDatabase();
        Cursor pCursor;

        switch (mUriMatcher.match(uri)) {
            case MOVIES_CODE:
                pCursor = database.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIES_WITH_ID_CODE:
                pCursor = database.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        pCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return pCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase pDb = mOpenHelper.getWritableDatabase();

        Uri returnUri;

        switch (mUriMatcher.match(uri)) {
            case MOVIES_WITH_ID_CODE:
                long num = pDb.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (num > 0) {
                    returnUri = ContentUris.withAppendedId(uri, num);
                } else {
                    returnUri = ContentUris.withAppendedId(uri, 2801);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted;

        if (selection == null) {
            selection = "1";
        }

        switch (mUriMatcher.match(uri)) {
            case MOVIES_WITH_ID_CODE:
                rowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
