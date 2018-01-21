package com.poras.passionate.awemovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by purus on 1/9/2018.
 */

public class Trailer implements Parcelable {

    private final String mTrailerId;
    private final String mLanguage;
    private final String mLanguageRegion;
    public final String mKey;
    public final String mName;
    private final String mSite;
    private final int mSize;
    private final String mType;

    public Trailer(String id, String lC, String lR, String key, String name, String site, int size, String type) {
        this.mTrailerId = id;
        this.mLanguage = lC;
        this.mLanguageRegion = lR;
        this.mKey = key;
        this.mName = name;
        this.mSite = site;
        this.mSize = size;
        this.mType = type;
    }

    private Trailer(Parcel in) {
        mTrailerId = in.readString();
        mLanguage = in.readString();
        mLanguageRegion = in.readString();
        mKey = in.readString();
        mName = in.readString();
        mSite = in.readString();
        mSize = in.readInt();
        mType = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTrailerId);
        dest.writeString(mLanguage);
        dest.writeString(mLanguageRegion);
        dest.writeString(mKey);
        dest.writeString(mName);
        dest.writeString(mSite);
        dest.writeInt(mSize);
        dest.writeString(mType);


    }
}
