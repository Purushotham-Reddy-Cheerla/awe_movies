package com.poras.passionate.awemovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by purus on 1/9/2018.
 */

public class Review implements Parcelable {

    private final String mReviewId;
    public final String mAuthor;
    public final String mComment;
    private final String mCommentUrl;


    public Review(String id, String author, String content, String url) {
        this.mReviewId = id;
        this.mAuthor = author;
        this.mComment = content;
        this.mCommentUrl = url;
    }

    private Review(Parcel in) {
        mReviewId = in.readString();
        mAuthor = in.readString();
        mComment = in.readString();
        mCommentUrl = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mReviewId);
        dest.writeString(mAuthor);
        dest.writeString(mComment);
        dest.writeString(mCommentUrl);
    }
}
