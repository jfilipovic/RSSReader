package com.example.jerko.rssfeedreader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.jerko.rssfeedreader.fragments.ChannelsFragment;
import com.example.jerko.rssfeedreader.fragments.FeedFragment;
import com.example.jerko.rssfeedreader.models.Channel;
import com.example.jerko.rssfeedreader.models.FeedItem;
import com.example.jerko.rssfeedreader.tasks.RSSFeedTask;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<Channel> channels;
    public Channel selectedChannel;
    private Fragment frag;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();

        Channel fakeChannel = new Channel();
        fakeChannel.setTitle("-- select the feed --");
        channels = new ArrayList<>();
        channels.add(fakeChannel);
        selectedChannel = fakeChannel;
        new RSSFeedTask("http://www.jutarnji.hr/rss/vijesti", this).execute();
        new RSSFeedTask("http://www.sciencemag.org/rss/news_current.xml", this).execute();

        setFragment("channel");

    }

    public void refreshData(Channel channel){
        if (channel != null){
            channels.add(channel);
            if (channel.getTitle() != null) Log.d("kanal", channel.getTitle());
            else Log.d("kanal", "nema");
            if (frag instanceof ChannelsFragment)((ChannelsFragment) frag).refreshAdapter();
            //todo notification
            if (channels.size() > 3)createNotification(channel.getTitle());
        } else {
            Toast.makeText(this, "Invalid feed URL", Toast.LENGTH_LONG).show();
        }

    }

    public void setFragment(String fragName){
        if (fragName.equals("channel"))
            frag = new ChannelsFragment();
        else
            frag = new FeedFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit);
        fragmentTransaction.replace(R.id.fragment_holder, frag);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (frag instanceof FeedFragment) setFragment("channel");
        else super.onBackPressed();
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void createNotification(String feedTitle) {

       // Intent notificationIntent = new Intent(this, MainActivity.class);
       // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.abc_btn_radio_material)
                        .setContentTitle("New RSS feed is added")
                        .setContentText(feedTitle)
                        .setAutoCancel(true)
                        .setTicker(feedTitle);
                        //.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder.setLights(Color.RED, 3000, 3000);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        mNotificationManager.notify(001, mBuilder.build());
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
    }

    public void setToolbarIcon(){
        if (frag instanceof FeedFragment){
            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha );
            toolbar.setNavigationOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setFragment("channel");
                        }
                    }

            );
            if (selectedChannel != null && selectedChannel.getTitle() != null){
                toolbar.setTitle(selectedChannel.getTitle());
            }
        } else {
            toolbar.setNavigationIcon(null);
            toolbar.setTitle(getResources().getString(R.string.app_name));
        }
    }
}
