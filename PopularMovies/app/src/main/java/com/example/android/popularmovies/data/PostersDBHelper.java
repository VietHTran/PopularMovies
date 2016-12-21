package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Viet on 11/24/2016.
 */
public class PostersDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = PostersDBHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "posters.db";
    private static final int DATABASE_VERSION = 2;
    public static final int INDEX_ID = 0;
    public static final int INDEX_TITLE = 1;
    public static final int INDEX_PLOT =2;
    public static final int INDEX_PIC_URL = 3;
    public static final int INDEX_RATING=4;
    public static final int INDEX_RELEASE_DATE=5;
    public static final int INDEX_MOVIE_ID=6;

    public PostersDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                PosterContract.PosterEntry.POSTER_TABLE + "(" +
                PosterContract.PosterEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PosterContract.PosterEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                PosterContract.PosterEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                PosterContract.PosterEntry.COLUMN_PIC_URL + " TEXT NOT NULL, " +
                PosterContract.PosterEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                PosterContract.PosterEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                PosterContract.PosterEntry.COLUMN_MOVIE_ID +" INTEGER NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PosterContract.PosterEntry.POSTER_TABLE);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                PosterContract.PosterEntry.POSTER_TABLE + "'");
        onCreate(sqLiteDatabase);
    }
}
