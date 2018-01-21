package com.poras.passionate.awemovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.poras.passionate.awemovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by purus on 1/7/2018.
 */

public class NetworkUtil {
    private static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String api_key = "api_key";
    private static final String language = "language";
    private static final String page = "page";
    private static final String mLanguage = "en-US";

    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/w500";
    private static final String YOUTUBE_TRAILER_THUMB_PATH = "http://img.youtube.com/vi/%s/hqdefault.jpg";
    private static final String TRAILER_REVIEW_BASE_PATH = "http://api.themoviedb.org/3/movie/%s/";

    private static final String YOUTUBE_VIDEO_PATH = "http://www.youtube.com/watch";
    private static final String YOUTUBE_QUERY = "v";


    public static URL buildYoutubeLink(String key) throws MalformedURLException {
        Uri uri = Uri.parse(YOUTUBE_VIDEO_PATH).buildUpon()
                .appendQueryParameter(YOUTUBE_QUERY, key)
                .build();
        URL url = null;
        url = new URL(uri.toString());
        return url;
    }

    public static URL buildUrl(String queryPath, int pageNum) {
        Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(queryPath)
                .appendQueryParameter(language, mLanguage)
                .appendQueryParameter(page, String.valueOf(pageNum))
                .appendQueryParameter(api_key, BuildConfig.MDB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static URL getTrailerReviewUrl(int id, String type) {
        Uri uri = Uri.parse(getBaseUrlWithMovieId(Integer.toString(id))).buildUpon()
                .appendPath(type)
                .appendQueryParameter(language, mLanguage)
                .appendQueryParameter(api_key, BuildConfig.MDB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String getMovieTrailerReviewFromUrl(URL url) throws IOException {
        if (url != null) {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");
                boolean hasContent = scanner.hasNext();
                if (hasContent) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                connection.disconnect();
            }
        } else {
            return null;
        }
    }

    public static String getTrailerThumbPath(String key) {
        return String.format(YOUTUBE_TRAILER_THUMB_PATH, key);
    }

    private static String getBaseUrlWithMovieId(String movieId) {
        return String.format(TRAILER_REVIEW_BASE_PATH, movieId);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            return !((info == null) || (info.getType() != ConnectivityManager.TYPE_MOBILE && info.getType() != ConnectivityManager.TYPE_WIFI)
                    || !(info.isConnected()));
        } else {
            return false;
        }

    }
}
