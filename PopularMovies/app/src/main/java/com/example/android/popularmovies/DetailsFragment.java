package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_details, container, false);
        Intent intent=getActivity().getIntent();
        ImageView poster= (ImageView) root.findViewById(R.id.details_poster);
        TextView title=(TextView) root.findViewById(R.id.details_title);
        TextView plot=(TextView) root.findViewById(R.id.details_plot);
        TextView rating=(TextView) root.findViewById(R.id.details_rating);
        TextView date=(TextView) root.findViewById(R.id.details_release_date);
        if (intent!=null) {
            Picasso.with(getActivity()).load(intent.getStringExtra(getString(R.string.fragment_pic_url))).into(poster);
            title.setText(intent.getStringExtra(getString(R.string.fragment_title)));
            plot.setText(intent.getStringExtra(getString(R.string.fragment_plot)));
            rating.setText(intent.getStringExtra(getString(R.string.fragment_rating)));
            date.setText(intent.getStringExtra(getString(R.string.fragment_release_date)));
        } else {
            title.setText("???");
            plot.setText("???");
            rating.setText("???");
            date.setText("???");
        }
        return root;
    }
}
