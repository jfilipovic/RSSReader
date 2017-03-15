package com.example.jerko.rssfeedreader.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jerko.rssfeedreader.MainActivity;
import com.example.jerko.rssfeedreader.R;
import com.example.jerko.rssfeedreader.models.Channel;
import com.example.jerko.rssfeedreader.tasks.RSSFeedTask;

import java.util.List;

/**
 * Created by Jerko on 13.3.2017..
 */

public class ChannelsFragment extends Fragment {

    private ChannelsSpinnerAdapter spinnerAdapter;
    private Spinner channelSpinner;
    private Button addButton;
    private EditText addEditText;
    private RelativeLayout parentLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_channels, container, false);
        ((MainActivity)getActivity()).setToolbarIcon();

        addButton = (Button) view.findViewById(R.id.add_button);
        addEditText = (EditText) view.findViewById(R.id.addurl_view);
        channelSpinner = (Spinner) view.findViewById(R.id.channel_spinner);
        parentLayout = (RelativeLayout) view.findViewById(R.id.channels_layout);

        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideKeyboard();
            }
        });

        spinnerAdapter = new ChannelsSpinnerAdapter(getActivity(), ((MainActivity)getActivity()).channels);
        channelSpinner.setAdapter(spinnerAdapter);
        channelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position > 0){
                    ((MainActivity)getActivity()).selectedChannel = ((MainActivity)getActivity()).channels.get(position);
                    ((MainActivity)getActivity()).setFragment("feed");
                    ((MainActivity)getActivity()).hideKeyboard();
                }
                Log.d("spinnerselect", ((MainActivity)getActivity()).channels.get(position).getTitle() );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = addEditText.getText().toString();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                new RSSFeedTask(url, getActivity()).execute();
                ((MainActivity)getActivity()).hideKeyboard();
            }
        });

        return view;
    }

    public class ChannelsSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
        private Activity activity;
        private List<Channel> channels;

        public ChannelsSpinnerAdapter(Activity activity, List<Channel> channels){
            this.activity = activity;
            this.channels = channels;
        }

        public int getCount() {
            return channels.size();
        }

        public Object getItem(int position) {
            return channels.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View spinView;
            if( convertView == null ){
                LayoutInflater inflater = activity.getLayoutInflater();
                spinView = inflater.inflate(R.layout.spinner_row, null);
            } else {
                spinView = convertView;
            }
            TextView title = (TextView) spinView.findViewById(R.id.spinner_row_title);
            title.setText(channels.get(position).getTitle());

            return spinView;
        }
    }

    public void refreshAdapter(){
        spinnerAdapter.notifyDataSetChanged();
    }
}
