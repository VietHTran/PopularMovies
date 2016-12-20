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

    public static Poster currentPoster;
    private int movieId;
    private final String YOUTUBE_URL_PREFIX="https://www.youtube.com/watch?v=";
    private final String TRAILER_KEY="trailer";
    private final String REVIEW_KEY="review";
    private ArrayList<Trailer> trailerList;
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<Review> reviewList;
    private ReviewAdapter mReviewAdapter;
    private static View mRoot;

    public DetailsFragment() {
    }
    private void getTrailerAsync() {
        if (movieId==0) return;
        FetchTrailersTask ftt= new FetchTrailersTask();
        ftt.execute(Integer.toString(movieId));
    }
    private void getReviewAsync () {
        if (movieId==0) return;
        FetchReviewsTask frt= new FetchReviewsTask();
        frt.execute(Integer.toString(movieId));
    }
    //Check if the device using is tablet
    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public static DetailsFragment newInstance() {

        Bundle args = new Bundle();
        args.putParcelable("poster",currentPoster);
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void changeMovie() {
        if (mRoot==null) return;

    }

    public Poster getShownPoster() {
        return getArguments().getParcelable("poster");
    }
    @Override
    public void onCreate(Bundle args) {
        super.onCreate(args);
        Intent intent=getActivity().getIntent();
        if (intent==null || intent.getData()==null) {
            return;
        }
    }
    //Save downloaded data in case of rotations
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelableArrayList(TRAILER_KEY,trailerList);
        bundle.putParcelableArrayList(REVIEW_KEY,reviewList);
        super.onSaveInstanceState(bundle);
    }

    public void updateContent() {
        if (mRoot==null || currentPoster==null) return;
        View view=mRoot.findViewById(R.id.fragment_detail_linear_layout);
        view.setVisibility(View.VISIBLE);
        movieId=currentPoster.movieId;
        ImageView poster= (ImageView) mRoot.findViewById(R.id.details_poster);
        TextView title=(TextView) mRoot.findViewById(R.id.details_title);
        TextView plot=(TextView) mRoot.findViewById(R.id.details_plot);
        TextView ratingView=(TextView) mRoot.findViewById(R.id.details_rating);
        TextView date=(TextView) mRoot.findViewById(R.id.details_release_date);
        final Button addFavorite=(Button) mRoot.findViewById(R.id.details_add_favorite);
        Picasso.with(getActivity()).load(currentPoster.picUrl).into(poster);
        Log.d("test","thisispicurl "+currentPoster.picUrl);
        title.setText(currentPoster.title);
        plot.setText(currentPoster.plot);
        ratingView.setText(currentPoster.rating);
        date.setText(currentPoster.releaseDate.substring(0,4));

        movieId=currentPoster.movieId;
        //Log.v("test","thisismovieid "+intent.getIntExtra(getString(R.string.fragment_id),0));

        addFavorite.setText(Favorite.favorites.containsKey(movieId)?
                getString(R.string.details_remove_favorite) :
                getString(R.string.details_add_favorite));
        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Favorite.favorites.containsKey(movieId)) {
                    Favorite.favoriteList.remove(currentPoster);
                    Favorite.favorites.remove(movieId);
                    addFavorite.setText(getString(R.string.details_add_favorite));
                } else {
                    Favorite.favoriteList.add(currentPoster);
                    Favorite.favorites.put(movieId,currentPoster);
                    addFavorite.setText(getString(R.string.details_remove_favorite));
                }
            }
        });
        if (MainActivity.mTwoPane) {
            getTrailerAsync();
            getReviewAsync();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRoot= inflater.inflate(R.layout.fragment_details, container, false);
        View view=mRoot.findViewById(R.id.fragment_detail_linear_layout);
        view.setVisibility(View.GONE);
        Intent intent=getActivity().getIntent();

        if (intent!=null) {
            updateContent();
            //If not tablet then set the layout orientation to vertical for the sake of readability
            if (!isTablet(getActivity())) {
                LinearLayout layout=(LinearLayout) mRoot.findViewById(R.id.details_data);
                layout.setOrientation(LinearLayout.VERTICAL);
            }
        } else {
//            title.setText("???");
//            plot.setText("???");
//            ratingView.setText("???");
//            date.setText("???");
            movieId=0;
        }

        if (savedInstanceState!=null&&savedInstanceState.containsKey(TRAILER_KEY)) {
            //Log.d("test","isDataSaved");
            trailerList=savedInstanceState.getParcelableArrayList(TRAILER_KEY);
            reviewList=savedInstanceState.getParcelableArrayList(REVIEW_KEY);
        } else {
            //Log.d("test","isDataSavedNot");
            trailerList= new ArrayList<Trailer>();
            reviewList= new ArrayList<Review>();
        }

        mTrailerAdapter= new TrailerAdapter(getActivity(),trailerList);
        getTrailerAsync();

        ListView trailersView = (ListView) mRoot.findViewById(R.id.details_trailer);
        trailersView.setAdapter(mTrailerAdapter);
        trailersView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer trailer=mTrailerAdapter.getItem(position);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL_PREFIX+trailer.url)));
            }
        });

        mReviewAdapter= new ReviewAdapter(getActivity(),reviewList);
        getReviewAsync();

        ListView reviewView = (ListView) mRoot.findViewById(R.id.details_review);
        reviewView.setAdapter(mReviewAdapter);
        return mRoot;
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
                //Log.d(TEST_TAG,"thisistrailer: "+trailer.getString(KEY));
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
    public class FetchReviewsTask extends AsyncTask<String, Void, Review[]> {
        private final String TEST_TAG="Testing";
        private Review[] getReviewsURLFromJSON(String jsonStr) throws JSONException {
            //JSON Keys
            final String RESULTS="results";
            final String AUTHOR="author";
            final String CONTENT="content";
            if (jsonStr==null) {
                throw new JSONException("Null JSON String");
            }
            JSONObject jsonObject= new JSONObject(jsonStr);
            JSONArray results= jsonObject.getJSONArray(RESULTS);
            Review[] output= new Review[results.length()];
            for (int i=0;i<results.length();i++) {
                JSONObject trailer= results.getJSONObject(i);
                output[i]=new Review(trailer.getString(AUTHOR),trailer.getString(CONTENT));
                Log.d("test",output[i].username);
            }
            return output;
        }
        @Override
        protected Review[] doInBackground(String... request) {
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
                        .appendPath("reviews")
                        .appendQueryParameter(API_KEY_LABEL,API_KEY)
                        .build();
                URL url = new URL(uri.toString());
                //Log.v(TEST_TAG,"thisisreviewuri "+uri.toString());
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
                return getReviewsURLFromJSON(trailersJsonStr);
            } catch (JSONException e) {
                //Get JSON Exception later
                Log.e("ERROR", "JSON Error", e);
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Review[] reviews) {
            if (reviews!=null) {

                //this is the first time
                if (reviewList.size()==0) {
                    reviewList.clear();
                    for (int i=0;i<reviews.length;i++) {
                        reviewList.add(reviews[i]);
                    }
                } else
                //this is the n>1 time
                {
                    reviewList.clear();
                    mReviewAdapter.clear();
                    for (int i=0;i<reviews.length;i++) {
                        mReviewAdapter.add(reviews[i]);
                        reviewList.add(reviews[i]);
                    }
                }
            }
        }
    }
}
