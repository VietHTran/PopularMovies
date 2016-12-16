package com.example.android.popularmovies;

/**
 * Created by Viet on 10/1/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.*;
import android.widget.*;

import com.example.android.popularmovies.data.PosterContract;
import com.squareup.picasso.Picasso;

import java.util.List;

//    public PosterAdapter(Activity context, List<Poster> posters) {
//        super(context,0,posters);
//    }
//    @Override
//    public View getView(int pos, View convertView, ViewGroup parent) {
//        Poster i=getItem(pos);
//        View root= LayoutInflater.from(getContext()).inflate(R.layout.list_item_poster,parent,false);
//        ImageView imageView= (ImageView)root.findViewById(R.id.list_item_poster_imageview);
//        Picasso.with(getContext()).load(i.picUrl).into(imageView);
//        return root;
//    }
//    public PosterAdapter(){
//
//    }
public class PosterAdapter extends CursorAdapter {
    private Context mContext;
    private static int sLoaderID;
    public static class ViewHolder {
        public final ImageView imageView;
        public ViewHolder (View view) {
            imageView = (ImageView) view.findViewById(R.id.list_item_poster_imageview);
        }
    }
    public PosterAdapter (Context context, Cursor cursor, int flags,int loaderId) {
        super(context,cursor,flags);
        mContext=context;
        sLoaderID=loaderId;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view=LayoutInflater.from(context).inflate(R.layout.list_item_poster,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder=(ViewHolder)view.getTag();
        int colId=cursor.getColumnIndex(PosterContract.PosterEntry.COLUMN_PIC_URL);
        String picUrl=cursor.getString(colId);
        Picasso.with(context).load(picUrl).into(viewHolder.imageView);
    }
}
