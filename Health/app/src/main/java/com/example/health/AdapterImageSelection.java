package com.example.health;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AdapterImageSelection extends BaseAdapter {
    private Context mContext;

    public AdapterImageSelection(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return mThumbIds[position];
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.item_image_selection, parent, false);

        ImageView image = view.findViewById(R.id.imageView);
        image.setImageResource(mThumbIds[position]);
        image.setTag(mThumbIds[position]);
        return view;
    }

    // references to our images
    public Integer[] mThumbIds = {
            R.drawable.card0, R.drawable.card1, R.drawable.card2,
            R.drawable.card3, R.drawable.card4, R.drawable.card5,
            R.drawable.card6, R.drawable.card7, R.drawable.card8,
            R.drawable.card9, R.drawable.card10, R.drawable.card11,
            R.drawable.card12, R.drawable.card13, R.drawable.card14,
            R.drawable.card15, R.drawable.card16, R.drawable.card17,
            R.drawable.card18, R.drawable.card19, R.drawable.card20,
            R.drawable.card21, R.drawable.card22, R.drawable.card23,
            R.drawable.card24 };
}