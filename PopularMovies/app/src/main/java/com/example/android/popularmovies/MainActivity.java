package com.example.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmovies.data.PosterContract;
import com.example.android.popularmovies.data.PosterProvider;
import com.example.android.popularmovies.data.PostersDBHelper;

public class MainActivity extends AppCompatActivity {

    public static final String DETAILFRAGMENT_TAG = "DFTAG";
    public static boolean mTwoPane;
    public static SQLiteDatabase database;
    private String mSortType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
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

    private void initData(){
        Favorite favorite= new Favorite();
        PostersDBHelper helper=new PostersDBHelper(this);
        database=helper.getWritableDatabase();
        Cursor cursor=helper.getReadableDatabase().query(
                PosterContract.PosterEntry.POSTER_TABLE,null,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                Poster poster= new Poster(
                        cursor.getString(PostersDBHelper.INDEX_PIC_URL),
                        cursor.getString(PostersDBHelper.INDEX_TITLE),
                        cursor.getString(PostersDBHelper.INDEX_PLOT),
                        cursor.getString(PostersDBHelper.INDEX_RATING),
                        cursor.getString(PostersDBHelper.INDEX_RELEASE_DATE),
                        cursor.getInt(PostersDBHelper.INDEX_MOVIE_ID)
                );
                Favorite.push(poster);
            } while (cursor.moveToNext());
        }
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
            Utility.updateMainActivity(this);
        }
    }
}
