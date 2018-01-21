package com.poras.passionate.awemovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by purus on 1/7/2018.
 */

public class Movie implements Parcelable {

    public final String posterPath;
    public final String review;
    public final String originalTitle;
    public final double userRating;
    public final String releaseDate;
    public final int movieId;
    public final Boolean mFavorite;

    public Movie(int id, String path, String title, String date, double rating, String overview, Boolean favorite) {
        this.movieId = id;
        this.posterPath = path;
        this.originalTitle = title;
        this.releaseDate = date;
        this.userRating = rating;
        this.review = overview;
        this.mFavorite = favorite;
    }

    private Movie(Parcel in) {
        movieId = in.readInt();
        posterPath = in.readString();
        originalTitle = in.readString();
        releaseDate = in.readString();
        userRating = in.readDouble();
        review = in.readString();
        mFavorite = in.readByte() != 0;

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(posterPath);
        dest.writeString(originalTitle);
        dest.writeString(releaseDate);
        dest.writeDouble(userRating);
        dest.writeString(review);
        dest.writeByte((byte) (mFavorite ? 1 : 0));
    }

}
