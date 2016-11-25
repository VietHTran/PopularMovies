package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Viet on 11/24/2016.
 */
public class PosterContract {
    public static final String CONTENT_AUTHORITY="com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final class PosterEntry implements BaseColumns {
        public static final String POSTER_TABLE="poster";
        public static final String _ID = "_id";
        public static final String COLUMN_PIC_URL = "pic_url";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PLOT ="plot";
        public static final String COLUMN_RATING="rating";
        public static final String COLUMN_RELEASE_DATE="release_date";
        public static final String COLUMN_MOVIE_ID="movie_id";
        public static final String COLUMN_SORT_TYPE="sort_type";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(POSTER_TABLE).build();
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + POSTER_TABLE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + POSTER_TABLE;
        public static Uri buildPostersUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
