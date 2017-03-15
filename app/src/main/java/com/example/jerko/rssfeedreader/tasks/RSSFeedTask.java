package com.example.jerko.rssfeedreader.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.jerko.rssfeedreader.MainActivity;
import com.example.jerko.rssfeedreader.models.Channel;
import com.example.jerko.rssfeedreader.models.FeedItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Jerko on 13.3.2017..
 */

public class RSSFeedTask extends AsyncTask<String, Void, Channel> {

    private String address;
    private URL url;
    private Activity activity;


    public RSSFeedTask(String address, Activity activity){
        this.address = address;
        this.activity = activity;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Channel doInBackground(String... params) {
        return parseXml(fetchData());
    }

    @Override
    protected void onPostExecute(Channel channel) {
        ((MainActivity) activity).refreshData(channel);
    }

    private Document fetchData(){
        try {
            url = new URL (address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDoc = builder.parse(inputStream);
            return xmlDoc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Channel parseXml (Document data){
        Channel feedChannel = new Channel();
        try{
            if (data != null){
                Element root = data.getDocumentElement();
                Node channel = root.getChildNodes().item(1);
                NodeList items = channel.getChildNodes();
                for (int i = 0; i < items.getLength(); i++){
                    Node currentChild = items.item(i);
                    if (currentChild.getNodeName().equalsIgnoreCase("title")) feedChannel.setTitle(currentChild.getTextContent());
                    if (currentChild.getNodeName().equalsIgnoreCase("link")) feedChannel.setLink(currentChild.getTextContent());

                    if (currentChild.getNodeName().equalsIgnoreCase("item")){
                        FeedItem feedItem = new FeedItem();
                        NodeList itemChilds = currentChild.getChildNodes();
                        for (int j = 0; j < itemChilds.getLength(); j++){
                            Node current = itemChilds.item(j);
                            Log.d("textcontent", current.getTextContent());

                            if (current.getNodeName().equalsIgnoreCase("title")) feedItem.setTitle(current.getTextContent());
                            if (current.getNodeName().equalsIgnoreCase("link")) feedItem.setLink(current.getTextContent());
                            if (current.getNodeName().equalsIgnoreCase("description")) feedItem.setDescription(current.getTextContent());
                            if (current.getNodeName().equalsIgnoreCase("media:thumbnail")
                                    || current.getNodeName().equalsIgnoreCase("enclosure")) {
                                String url = current.getAttributes().item(0).getTextContent();
                                feedItem.setImageSrc(url);
                            }
                        }
                        feedChannel.getFeedItems().add(feedItem);
                    }

                }

                return feedChannel;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
