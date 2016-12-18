package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Viet on 12/17/2016.
 */
public class Trailer implements Parcelable {
    public String url;
    public Trailer(String url) {
        this.url=url;
    }
    public Trailer(Parcel parcel) {
        url=parcel.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }
    public  final Parcelable.Creator<Trailer> CREATOR= new Parcelable.Creator<Trailer>(){
        @Override
        public  Trailer createFromParcel(Parcel parcel) {
            return  new Trailer(parcel);
        }
        @Override
        public  Trailer[] newArray(int i) {
            return new Trailer[i];
        }
    };
}
