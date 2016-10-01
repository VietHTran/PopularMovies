package com.example.android.popularmovies;

/**
 * Created by Viet on 10/1/2016.
 */
import android.app.Activity;
import android.view.*;
import android.widget.*;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PosterAdapter extends ArrayAdapter<Poster> {
    public PosterAdapter(Activity context, List<Poster> posters) {
        super(context,0,posters);
    }
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        Poster i=getItem(pos);
        View root= LayoutInflater.from(getContext()).inflate(R.layout.list_item_poster,parent,false);
        ImageView imageView= (ImageView)root.findViewById(R.id.list_item_poster_imageview);
        Picasso.with(getContext()).load(i.picUrl).into(imageView);
        return root;
    }
}
