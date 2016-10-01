package com.example.android.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    //
    //Get popular movies: http://api.themoviedb.org/3/movie/popular?api_key=[]
    //Get top-rated movies: http://api.themoviedb.org/3/movie/top_rated?api_key=[]
    private PosterAdapter mPosterAdapter;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ArrayList<Poster> urlSamples= new ArrayList<Poster>();
        //testing
        for (int i=0;i<10;i++) {
            urlSamples.add(new Poster("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"));
        }
        mPosterAdapter= new PosterAdapter(getActivity(),urlSamples);
        ListView listView = (ListView) root.findViewById(R.id.listview_poster);
        listView.setAdapter(mPosterAdapter);
        return root;
    }
}
