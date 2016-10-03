package com.example.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    //
    //Get popular movies: http://api.themoviedb.org/3/movie/popular?api_key=[]
    //Get top-rated movies: http://api.themoviedb.org/3/movie/top_rated?api_key=[]
    private PosterAdapter mPosterAdapter;
    private ArrayList<Poster> urlSamples;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        urlSamples= new ArrayList<Poster>();
        //testing
        for (int i=0;i<15;i++) {
            urlSamples.add(new Poster("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"));
        }
        mPosterAdapter= new PosterAdapter(getActivity(),urlSamples);
        GridView gridView = (GridView) root.findViewById(R.id.gridview_poster);
        gridView.setAdapter(mPosterAdapter);
        return root;
    }

    public class FettchMoviesTask extends AsyncTask<String, Void, String[]> {
        private final String TEST_TAG="Testing";
        private String[] getMoviesDataFromJSON(String jsonStr) {
            return new String[10];
        }
        @Override
        protected  String[] doInBackground(String... request) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            //Please paste your API Key to the constant below
            final String API_KEY="";
            final String API_KEY_LABEL="api_key";
            try {
                //Implement preference for sortByLater
                String sortBy="top-rated";
                Uri uri= new Uri.Builder().scheme("http").authority("api.themoviedb.org").appendPath("3").appendPath("movie").appendPath(sortBy)
                        .appendQueryParameter(API_KEY_LABEL,API_KEY)
                        .build();
                URL url = new URL(uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //Log.v(TEST_TAG,"movies string below");
                moviesJsonStr=buffer.toString();
                //Log.v(TEST_TAG,"moviesstringhere: "+moviesJsonStr);

            } catch (IOException e) {

            } finally {
                if (urlConnection!=null) {
                    urlConnection.disconnect();
                }
                if (reader!=null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.v("ERROR","Error in closing reader",e);
                    }
                }
            }
            try {
                return getMoviesDataFromJSON(moviesJsonStr);
            } catch (Exception e) {
                //Get JSON Exception later
                Log.e("ERROR", "JSON Error", e);
                e.printStackTrace();
                return null;
            }
        }
    }
}
