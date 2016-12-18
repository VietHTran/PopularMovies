package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Viet on 11/24/2016.
 */
public class PosterProvider extends ContentProvider {
    private static final String LOG_TAG = PosterProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private PostersDBHelper mOpenHelper;
    static final int POSTER=1;
    static final int POSTER_WITH_ID=2;
    @Override
    public boolean onCreate() {
        mOpenHelper=new PostersDBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case POSTER:
                cursor=mOpenHelper.getReadableDatabase().query(PosterContract.PosterEntry.POSTER_TABLE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return cursor;
            case POSTER_WITH_ID:
                cursor=mOpenHelper.getReadableDatabase().query(PosterContract.PosterEntry.POSTER_TABLE,
                        projection,
                        PosterContract.PosterEntry._ID+" =?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return cursor;
            default:
                throw new UnsupportedOperationException("Invalid URI: "+uri);
        }
    }
    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match){
            case POSTER:{
                return PosterContract.PosterEntry.CONTENT_DIR_TYPE;
            }
            case POSTER_WITH_ID:{
                return PosterContract.PosterEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri res;
        switch (sUriMatcher.match(uri)) {
            case POSTER:
                long id=db.insert(PosterContract.PosterEntry.POSTER_TABLE,null,values);
                if (id > 0) {
                    res = PosterContract.PosterEntry.buildPostersUri(id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return res;
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case POSTER:
                count = db.delete(
                        PosterContract.PosterEntry.POSTER_TABLE, selection, selectionArgs);
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        PosterContract.PosterEntry.POSTER_TABLE + "'");
                break;
            case POSTER_WITH_ID:
                count = db.delete(PosterContract.PosterEntry.POSTER_TABLE,
                        PosterContract.PosterEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        PosterContract.PosterEntry.POSTER_TABLE + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return count;
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count = 0;
        if (values == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }
        switch(sUriMatcher.match(uri)){
            case POSTER:{
                count = db.update(PosterContract.PosterEntry.POSTER_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case POSTER_WITH_ID: {
                count = db.update(PosterContract.PosterEntry.POSTER_TABLE,
                        values,
                        PosterContract.PosterEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (count > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch(sUriMatcher.match(uri)){
            case POSTER:
                db.beginTransaction();

                int count = 0;
                try{
                    for(ContentValues value : values){
                        if (value == null){
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try{
                            _id = db.insertOrThrow(PosterContract.PosterEntry.POSTER_TABLE,
                                    null, value);
                        }catch(SQLiteConstraintException e) {
                            Log.w(LOG_TAG, "Attempting to insert but value is already in database.");
                        }
                        if (_id != -1){
                            count++;
                        }
                    }
                    if(count> 0){
                        db.setTransactionSuccessful();
                    }
                } finally {
                    db.endTransaction();
                }
                if (count > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count;
            default:
                return super.bulkInsert(uri, values);
        }
    }
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        String authority= PosterContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, PosterContract.PosterEntry.POSTER_TABLE,POSTER);
        matcher.addURI(authority, PosterContract.PosterEntry.POSTER_TABLE+"/#",POSTER_WITH_ID);
        return matcher;
    }
}
