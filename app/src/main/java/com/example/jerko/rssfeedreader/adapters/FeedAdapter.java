package com.example.jerko.rssfeedreader.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jerko.rssfeedreader.R;
import com.example.jerko.rssfeedreader.models.FeedItem;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Jerko on 14.3.2017..
 */

public class FeedAdapter extends ArrayAdapter<FeedItem> {
    private Activity activity;
    private List<FeedItem> feedItems;

    public FeedAdapter(Activity activity, List<FeedItem> feedItems){
        super(activity, R.layout.feeditem_row, feedItems);
        this.activity = activity;
        this.feedItems = feedItems;
    }

    public int getCount() {
        return feedItems.size();
    }

    public FeedItem getItem(int position) {
        return feedItems.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if( convertView == null ){
            holder = new ViewHolder();
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.feeditem_row, null);

            holder.titleHolder = (TextView) convertView.findViewById(R.id.titleHolder);
            holder.descriptionHolder = (TextView) convertView.findViewById(R.id.descriptionHolder);
            //image
            holder.imageHolder = (ImageView) convertView.findViewById(R.id.imageHolder);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FeedItem item = getItem(position);
        holder.titleHolder.setText(item.getTitle());
        holder.descriptionHolder.setText(item.getDescription());
        Picasso.with(getContext()).load(item.getImageSrc()).into(holder.imageHolder);

        return convertView;
    }



    static class ViewHolder {
        private TextView titleHolder;
        private TextView descriptionHolder;
        private ImageView imageHolder;
    }
}