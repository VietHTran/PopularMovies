package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.popularmovies.data.PosterContract;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Viet on 12/18/2016.
 */
public class Favorite {
    public static HashMap<Integer,Poster> favorites;
    public static ArrayList<Poster> favoriteList;
    public static SQLiteDatabase database;
    public Favorite(){
        favorites= new HashMap<Integer,Poster>();
        favoriteList= new ArrayList<Poster>();
    }
    public static void push(Poster poster) {
        favoriteList.add(poster);
        favorites.put(poster.movieId,poster);

        //MainActivity during initialization
        if (DetailsFragment.currentPoster==null) return;

        ContentValues posterValues= new ContentValues();
        posterValues.put(PosterContract.PosterEntry.COLUMN_TITLE,poster.title);
        posterValues.put(PosterContract.PosterEntry.COLUMN_PLOT,poster.plot);
        posterValues.put(PosterContract.PosterEntry.COLUMN_PIC_URL,poster.picUrl);
        posterValues.put(PosterContract.PosterEntry.COLUMN_RELEASE_DATE,poster.releaseDate);
        posterValues.put(PosterContract.PosterEntry.COLUMN_RATING,poster.rating);
        posterValues.put(PosterContract.PosterEntry.COLUMN_MOVIE_ID,poster.movieId);

        MainActivity.database.insert(PosterContract.PosterEntry.POSTER_TABLE,null,posterValues);
    }
    public static void remove(Poster poster) {
        for (int i=0;i<favoriteList.size();i++) {
            Poster holder= favoriteList.get(i);
            if (holder.movieId==poster.movieId) {
                favoriteList.remove(i);
                break;
            }
        }
        favorites.remove(poster.movieId);

        MainActivity.database.delete(PosterContract.PosterEntry.POSTER_TABLE,
                PosterContract.PosterEntry.COLUMN_MOVIE_ID+"="+poster.movieId,null);
    }

}
