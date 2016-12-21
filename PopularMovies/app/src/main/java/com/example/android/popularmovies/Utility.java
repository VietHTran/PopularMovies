package com.example.android.popularmovies;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

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
    public static void updateMainActivity(FragmentActivity fragmentActivity) {
        MainActivityFragment mf = (MainActivityFragment)fragmentActivity.getSupportFragmentManager().findFragmentById(R.id.fragment);
        if ( null != mf ) {
            mf.onSortTypeChanged();
        }
        if (!MainActivity.mTwoPane) return;
        //Log.d("test","thisisupdatecontent1");
        DetailsFragment.currentPoster=null;
        fragmentActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_detail_container,new DetailsFragment() , MainActivity.DETAILFRAGMENT_TAG)
                .commit();
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
