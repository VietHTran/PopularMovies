package com.example.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

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
    public void updateMovies() {
        //Implement preference later
        String request="top_rated";
        FetchMoviesTask fmt= new FetchMoviesTask();
        fmt.execute(request);
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
        updateMovies();
        mPosterAdapter= new PosterAdapter(getActivity(),urlSamples);
        GridView gridView = (GridView) root.findViewById(R.id.gridview_poster);
        gridView.setAdapter(mPosterAdapter);
        return root;
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Poster[]> {
        private final String TEST_TAG="Testing";
        private Poster[] getMoviesDataFromJSON(String jsonStr) throws JSONException {
            //JSON Keys
            final String RESULTS="results";
            final String ORIGINAL_LINK="http://image.tmdb.org/t/p/w185";
            final String POSTER_PATH="poster_path";
            final String TITLE="original_title";
            final String OVERVIEW="overview";
            final String RATING="vote_average";
            final String DATE="release_date";

            JSONObject jsonObject= new JSONObject(jsonStr);
            JSONArray results= jsonObject.getJSONArray(RESULTS);
            Poster[] output= new Poster[results.length()];
            for (int i=0;i<results.length();i++) {
                JSONObject movie= results.getJSONObject(i);
                StringBuilder builder= new StringBuilder(ORIGINAL_LINK);
                builder.append(movie.getString(POSTER_PATH));
                output[i]= new Poster(builder.toString(),
                        movie.getString(TITLE),
                        movie.getString(OVERVIEW),
                        Double.toString(movie.getDouble(RATING)),
                        movie.getString(DATE));
            }
            Log.v(TEST_TAG,"teken10");
            return output;
        }
        @Override
        protected  Poster[] doInBackground(String... request) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            //Please paste your API Key to the constant below
            final String API_KEY="";
            final String API_KEY_LABEL="api_key";
            try {
                //Implement preference for sortByLater
                String sortBy=request[0];
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
                moviesJsonStr=buffer.toString();

            } catch (IOException e) {
                Log.v(TEST_TAG,"Error IO",e);
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
            } catch (JSONException e) {
                //Get JSON Exception later
                Log.e("ERROR", "JSON Error", e);
                e.printStackTrace();
                return null;
            }
        }

    }
}
