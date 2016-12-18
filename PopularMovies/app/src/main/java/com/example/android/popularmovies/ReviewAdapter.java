package com.example.android.popularmovies;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Viet on 12/18/2016.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {
    public ReviewAdapter(Activity context, List<Review> Review) {
        super(context,0,Review);
    }
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        Review i=getItem(pos);
        View root= LayoutInflater.from(getContext()).inflate(R.layout.list_item_review,parent,false);
        TextView username=(TextView) root.findViewById(R.id.review_username);
        TextView content=(TextView) root.findViewById(R.id.review_content);
        username.setText(i.username);
        content.setText(i.content);
        return root;
    }
}
