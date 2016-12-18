package com.example.android.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Viet on 12/17/2016.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {
    private final String PREFIX="Trailer ";
    public TrailerAdapter(Activity context, List<Trailer> posters) {
        super(context,0,posters);
    }
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        Trailer i=getItem(pos);
        View root= LayoutInflater.from(getContext()).inflate(R.layout.list_item_trailer,parent,false);
        TextView textView=(TextView)root.findViewById(R.id.trailer_text);
        textView.setText(PREFIX+(pos+1));
        return root;
    }
}
