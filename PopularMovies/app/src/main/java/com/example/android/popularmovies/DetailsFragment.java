package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class DetailsFragment extends Fragment {

    private int movieId;
    private final String YOUTUBE_URL_PREFIX="https://www.youtube.com/watch?v=";
    private ArrayList<Trailer> trailerList;
    private TrailerAdapter mTrailerAdapter;
    public DetailsFragment() {
    }
    private void getTrailerAsync() {
        FetchTrailersTask ftt= new FetchTrailersTask();
        ftt.execute(Integer.toString(movieId));
    }
    //Check if the device using is tablet
    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Use savedInstanceState later

        View root= inflater.inflate(R.layout.fragment_details, container, false);
        Intent intent=getActivity().getIntent();
        ImageView poster= (ImageView) root.findViewById(R.id.details_poster);
        TextView title=(TextView) root.findViewById(R.id.details_title);
        TextView plot=(TextView) root.findViewById(R.id.details_plot);
        TextView rating=(TextView) root.findViewById(R.id.details_rating);
        TextView date=(TextView) root.findViewById(R.id.details_release_date);
        Button addFavorite=(Button) root.findViewById(R.id.details_add_favorite);

        if (intent!=null) {

            Picasso.with(getActivity()).load(intent.getStringExtra(getString(R.string.fragment_pic_url))).into(poster);
            title.setText(intent.getStringExtra(getString(R.string.fragment_title)));
            plot.setText(intent.getStringExtra(getString(R.string.fragment_plot)));
            rating.setText(intent.getStringExtra(getString(R.string.fragment_rating)));
            date.setText(intent.getStringExtra(getString(R.string.fragment_release_date)).substring(0,4));
            addFavorite.setText(intent.getIntExtra(getString(R.string.fragment_favorite),0)==0
                    ? getString(R.string.details_add_favorite): getString(R.string.details_remove_favorite));

            movieId=intent.getIntExtra(getString(R.string.fragment_id),0);
            Log.v("test","thisismovieid "+intent.getIntExtra(getString(R.string.fragment_id),0));
            //If not tablet then set the layout orientation to vertical for the sake of readability
            if (!isTablet(getActivity())) {
                LinearLayout layout=(LinearLayout) root.findViewById(R.id.details_data);
                layout.setOrientation(LinearLayout.VERTICAL);
            }
            getTrailerAsync();
        } else {
            title.setText("???");
            plot.setText("???");
            rating.setText("???");
            date.setText("???");
            movieId=0;
        }

        trailerList= new ArrayList<Trailer>();
        mTrailerAdapter= new TrailerAdapter(getActivity(),trailerList);
        getTrailerAsync();

        ListView trailersView = (ListView) root.findViewById(R.id.details_trailer);
        trailersView.setScrollContainer(false);
        trailersView.setAdapter(mTrailerAdapter);
        trailersView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer trailer=mTrailerAdapter.getItem(position);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL_PREFIX+trailer.url)));
            }
        });

        return root;
    }
    public class FetchTrailersTask extends AsyncTask<String, Void, Trailer[]> {
        private final String TEST_TAG="Testing";
        private Trailer[] getTrailersURLFromJSON(String jsonStr) throws JSONException {
            //JSON Keys
            final String RESULTS="results";
            final String KEY="key";
            if (jsonStr==null) {
                throw new JSONException("Null JSON String");
            }
            JSONObject jsonObject= new JSONObject(jsonStr);
            JSONArray results= jsonObject.getJSONArray(RESULTS);
            Trailer[] output= new Trailer[results.length()];
            for (int i=0;i<results.length();i++) {
                JSONObject trailer= results.getJSONObject(i);
                output[i]=new Trailer(trailer.getString(KEY));
                //Log.v(TEST_TAG,"thisistrailer: "+trailer.getString(KEY));
            }
            return output;
        }
        @Override
        protected Trailer[] doInBackground(String... request) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String trailersJsonStr = null;
            /////////////////////////////////////////////////////
            //                                                 //
            // Please paste your API Key to the constant below //
            //                                                 //
            ////////////////////////////////////////////////////
            final String API_KEY="72f7940738a9f58a23116128df8550be";
            final String API_KEY_LABEL="api_key";
            try {
                //Implement preference for sortByLater
                String id=request[0];
                Uri uri= new Uri.Builder().scheme("http").authority("api.themoviedb.org").appendPath("3")
                        .appendPath("movie")
                        .appendPath(id)
                        .appendPath("videos")
                        .appendQueryParameter(API_KEY_LABEL,API_KEY)
                        .build();
                URL url = new URL(uri.toString());
                //Log.v(TEST_TAG,"thisisuri "+uri.toString());
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
                trailersJsonStr=buffer.toString();

            } catch (IOException e) {
                Log.v(TEST_TAG,"Error IO Connect",e);
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
                return getTrailersURLFromJSON(trailersJsonStr);
            } catch (JSONException e) {
                //Get JSON Exception later
                Log.e("ERROR", "JSON Error", e);
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Trailer[] urls) {
            if (urls!=null) {
                //this is the first time
                if (trailerList.size()==0) {
                    trailerList.clear();
                    for (int i=0;i<urls.length;i++) {
                        trailerList.add(urls[i]);
                    }
                } else
                //this is the n>1 time
                {
                    trailerList.clear();
                    mTrailerAdapter.clear();
                    for (int i=0;i<urls.length;i++) {
                        mTrailerAdapter.add(urls[i]);
                        trailerList.add(urls[i]);
                    }
                }
            }
        }
    }
}
