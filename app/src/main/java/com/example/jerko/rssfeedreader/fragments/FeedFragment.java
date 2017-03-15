package com.example.jerko.rssfeedreader.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jerko.rssfeedreader.MainActivity;
import com.example.jerko.rssfeedreader.R;
import com.example.jerko.rssfeedreader.adapters.FeedAdapter;
import com.example.jerko.rssfeedreader.models.FeedItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jerko on 13.3.2017..
 */

public class FeedFragment extends Fragment {

    private ListView listView;
    private List<FeedItem> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ((MainActivity)getActivity()).setToolbarIcon();

        items = new ArrayList<>(((MainActivity) getActivity()).selectedChannel.getFeedItems());
        listView = (ListView) view.findViewById(R.id.feedItemsListView);
        FeedAdapter adapter = new FeedAdapter(getActivity(), items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                openLinkInBrowser(items.get(position).getLink());

            }
        });

        return view;
    }

    public void openLinkInBrowser(String url) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }


    }
}