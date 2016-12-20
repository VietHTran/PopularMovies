package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    public static boolean mTwoPane;
    private String mSortType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Favorite favorite= new Favorite();
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState==null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_container,new DetailsFragment() , DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
        MainActivityFragment maf = (MainActivityFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        mSortType=Utility.getSortType(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void updateDetail() {
        DetailsFragment df = (DetailsFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
        df.updateContent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        String sortType=Utility.getSortType(this);
        if (sortType != null && !sortType.equals(mSortType)) {
            MainActivityFragment ff = (MainActivityFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
            if ( null != ff ) {
                ff.onSortTypeChanged();
            }
            mSortType = sortType;
        }
    }
}
