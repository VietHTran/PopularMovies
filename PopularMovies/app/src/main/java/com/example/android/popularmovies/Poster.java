package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Viet on 10/1/2016.
 */
public class Poster implements Parcelable {
    public String picUrl,title,plot,rating,releaseDate;
    public Poster (String u) {
        picUrl=u;
    }
    public Poster (String picUrl,String title,String plot,String rating, String releaseDate) {
        this.picUrl=picUrl;
        this.title=title;
        this.plot=plot;
        this.rating=rating;
        this.releaseDate=releaseDate;
    }
    public Poster (Parcel parcel) {
        picUrl=parcel.readString();
        title=parcel.readString();
        plot=parcel.readString();
        rating=parcel.readString();
        releaseDate=parcel.readString();
    }
    @Override
    public int describeContents() {
        return  0;
    }
    @Override
    public  void writeToParcel(Parcel d, int flags) {
        d.writeString(picUrl);
        d.writeString(title);
        d.writeString(plot);
        d.writeString(rating);
        d.writeString(releaseDate);
    }
    public  final Parcelable.Creator<Poster> CREATOR= new Parcelable.Creator<Poster>(){
        @Override
        public  Poster createFromParcel(Parcel parcel) {
            return  new Poster(parcel);
        }
        @Override
        public  Poster[] newArray(int i) {
            return new Poster[i];
        }
    };
}
