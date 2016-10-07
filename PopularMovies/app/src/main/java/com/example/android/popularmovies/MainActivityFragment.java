package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    //
    //Get popular movies: http://api.themoviedb.org/3/movie/popular?api_key=[]
    //Get top-rated movies: http://api.themoviedb.org/3/movie/top_rated?api_key=[]
    private PosterAdapter mPosterAdapter;
    private ArrayList<Poster> posterList;
    private final String POSTERS_KEY="posters";
    private String sortType="";
    public void updateMovies() {
        if (sortType.equals("")) {
            sortType= PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .getString(getString(R.string.pref_key_sort),getString(R.string.pref_top_rated));
        }
        FetchMoviesTask fmt= new FetchMoviesTask();
        fmt.execute(sortType);
    }
    public MainActivityFragment() {

    }

    //Setup the menu call by setHasOptionsMenu(true);
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu,menuInflater);
        menuInflater.inflate(R.menu.menu_main_fragment,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return true;
    }
    //Save downloaded data in case of rotation
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelableArrayList(POSTERS_KEY,posterList);
        super.onSaveInstanceState(bundle);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //Load saved bundle to ArrayList
        if (savedInstanceState==null || !savedInstanceState.containsKey(POSTERS_KEY)) {
            posterList= new ArrayList<Poster>();
        } else {
            posterList=savedInstanceState.getParcelableArrayList(POSTERS_KEY);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        mPosterAdapter= new PosterAdapter(getActivity(), posterList);
        if (savedInstanceState==null || !savedInstanceState.containsKey(POSTERS_KEY)) {
            updateMovies();
        }
        GridView gridView = (GridView) root.findViewById(R.id.gridview_poster);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getActivity(),Details.class);
                intent.putExtra(getString(R.string.fragment_pic_url),mPosterAdapter.getItem(position).picUrl)
                        .putExtra(getString(R.string.fragment_title),mPosterAdapter.getItem(position).title)
                        .putExtra(getString(R.string.fragment_plot),mPosterAdapter.getItem(position).plot)
                        .putExtra(getString(R.string.fragment_rating),mPosterAdapter.getItem(position).rating)
                        .putExtra(getString(R.string.fragment_release_date),mPosterAdapter.getItem(position).releaseDate);
                startActivity(intent);
            }
        });
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
            return output;
        }
        @Override
        protected  Poster[] doInBackground(String... request) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            /////////////////////////////////////////////////////
            //                                                 //
            // Please paste your API Key to the constant below //
            //                                                 //
            ////////////////////////////////////////////////////
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
        @Override
        protected void onPostExecute(Poster[] posters) {
            if (posters!=null) {
                //this is the first time
                if (posterList.size()==0) {
                    posterList.clear();
                    for (int i=0;i<posters.length;i++) {
                        posterList.add(posters[i]);
                    }
                } else
                //this is the n>1 time
                {
                    mPosterAdapter.clear();
                    //posterList.clear();
                    for (int i=0;i<posters.length;i++) {
                        mPosterAdapter.add(posters[i]);
                        //posterList.add(posters[i]);
                    }
                }
            }
        }
    }
}
