package com.example.android.popularmovies;

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
    public static String reformatDate(String date) {
        return date.substring(0,4);
    }
}
