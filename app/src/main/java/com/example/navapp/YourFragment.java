package com.example.navapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.navapp.Home.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YourFragment extends Fragment  {

    private static final String TAG = "YourFragment";
    ArrayList<String> allques;
    ArrayList<String> allquesLink;
    JSONArray arr;
    Map<String, String> map;

    private Context context=getContext();

    private ListView listView;
    public static MyAppDatabase myAppDatabase;

    private WebView webView;
    NavigationView navigationView;

    Menu menu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_your,container,false);


        allques= new ArrayList<String>();
        allquesLink= new ArrayList<String>();
        arr=new JSONArray();
        map = new HashMap<String, String>();
        webView=(WebView) view.findViewById(R.id.webView);

        listView = (ListView) view.findViewById(R.id.lvYour);

        navigationView = (NavigationView) view.findViewById(R.id.nav_view);

        allques.clear();
        allquesLink.clear();

        // get menu from navigationView
       // menu = navigationView.getMenu();

        Log.d(TAG, "onCreateView: ******************************************************************");
        Log.d(TAG, "onCreateView: "+getCheckedItem());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String theQuesURL=allquesLink.get(position);

                Log.d(TAG, "onItemClick: Navigating to Stackoverflow website to view question");
              //  webView=(WebView) view.findViewById(R.id.webView);
                listView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);

                WebSettings webSettings=webView.getSettings();
                webSettings.setJavaScriptEnabled(true);

                Log.d(TAG, "onCreate: Loading webpage..");

                webView.loadUrl(theQuesURL);

                Log.d(TAG, "onCreate: webpage loaded");

                webView.setWebViewClient(new WebViewClient());


                // tvtag.setText(allTags);




            }
        });
        myAppDatabase= Room.databaseBuilder(getContext(),MyAppDatabase.class,"tagsdb").allowMainThreadQueries().build();
        //deleteTags();
        //checktags();
        List<Tags> tags=myAppDatabase.myDao().getTags();


        try{

            String path1="https://api.stackexchange.com/2.2/tags/";
            String tagpath=tags.get(getCheckedItem()).getTag_name();
            String path2="/faq?site=stackoverflow";
            String ourURL=path1+tagpath+path2;

            Log.d(TAG, "onCreateView: "+ourURL);

            new JsonTask().execute(ourURL);

            JsonTask j=new JsonTask();
            j.execute(" ");
        }
        catch (Exception e)
        {
            Log.d(TAG, "onCreateView: "+e.getMessage());
        }
        //new JsonTask().execute("https://api.stackexchange.com/2.2/tags/java/faq?site=stackoverflow");





        return view;
    }

    public void updateJSON(int menuitem)
    {
        List<Tags> tags=myAppDatabase.myDao().getTags();
        try{

            String path1="https://api.stackexchange.com/2.2/tags/";
            String tagpath=tags.get(menuitem).getTag_name();
            String path2="/faq?site=stackoverflow";
            String ourURL=path1+tagpath+path2;

            Log.d(TAG, "onCreateView: "+ourURL);

            new JsonTask().execute(ourURL);

            JsonTask j=new JsonTask();
            j.execute(" ");
        }
        catch (Exception e)
        {
            Log.d(TAG, "onCreateView: "+e.getMessage());
        }
    }



    public static void updateList(int menuitem)
    {


    }


    private void getTagsFromURL(String line)
    {

        try {
//            JSONObject object=new JSONObject(line);
//            String itag=object.getJSONObject("items").getString("name");
//            Log.d(TAG, "getTagsFromURL: "+itag);

            JSONObject object1=new JSONObject(line);
            arr = object1.getJSONArray("items");
            for (int i = 0; i < arr.length(); i++) {
                String ques_id = arr.getJSONObject(i).getString("title");
                String ques_link = arr.getJSONObject(i).getString("link");
                Log.d(TAG, "getTagsFromURL: array "+ques_id);
                allques.add(ques_id);

                Log.d(TAG, "getTagsFromURL: array link :"+ques_link);
                allquesLink.add(ques_link);

                //map.put(ques_id,ques_link);
            }

        }
        catch (JSONException e)
        {
            Log.d(TAG, "getTagsFromURL: JSONException"+e.getMessage());
        }
        catch (NullPointerException e)
        {
            Log.d(TAG, "getTagsFromURL: NullPointerException");
        }


    }



    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");

                    largeLog(TAG,line);

                }

                getTagsFromURL(buffer.toString());


                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            if (pd.isShowing()){
//                pd.dismiss();
//            }
//            txtJson.setText(result);
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, allques);


            listView.setAdapter(itemsAdapter);
            for (int i = 0; i < arr.length(); i++) {

                Log.d(TAG, "onCreate: all questions "+i+" "+allques.get(i));
            }



//            for (Map.Entry<String,String> entry : map.entrySet()) {
//                Log.d(TAG, "onPostExecute: "+ entry.getKey()+"\n"+ entry.getValue());
//            }
       }
    }

    public static void largeLog(String tag, String content) {
        if (content.length() > 4000) {
            Log.d(tag, content.substring(0, 4000)+"\n");
            largeLog(tag, content.substring(4000));
        } else {
            Log.d(tag, content);
        }
    }

    private int getCheckedItem() {

        int i = MainActivity.menuitem;
        return i;
    }

}
