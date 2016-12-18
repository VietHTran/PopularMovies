package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Viet on 12/18/2016.
 */
public class Review implements Parcelable {
    public String username,content;
    public Review(String username,String content) {
        this.username=username;
        this.content=content;
    }
    public Review(Parcel parcel) {
        username=parcel.readString();
        content=parcel.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(content);
    }
    public  final Parcelable.Creator<Review> CREATOR= new Parcelable.Creator<Review>(){
        @Override
        public  Review createFromParcel(Parcel parcel) {
            return  new Review(parcel);
        }
        @Override
        public  Review[] newArray(int i) {
            return new Review[i];
        }
    };
}
