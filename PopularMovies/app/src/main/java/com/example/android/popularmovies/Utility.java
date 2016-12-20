package com.example.android.popularmovies;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Viet on 11/23/2016.
 */
public class Utility {
    public static String reformatRating(String rating) {
        if (rating.charAt(1)!='.') {
            return rating.substring(0,3);
        } else {
            return rating.substring(0,4);
        }
    }
    public static String getSortType(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_key_sort)
                        ,context.getString(R.string.pref_top_rated));
    }
    public static String reformatDate(String date) {
        return date.substring(0,4);
    }
}
