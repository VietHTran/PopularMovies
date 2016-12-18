package com.example.android.popularmovies;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Viet on 12/18/2016.
 */
public class Favorite {
    public static HashMap<Integer,Poster> favorites;
    public static ArrayList<Poster> favoriteList;
    public Favorite(){
        favorites= new HashMap<Integer,Poster>();
        favoriteList= new ArrayList<Poster>();
    }
}
