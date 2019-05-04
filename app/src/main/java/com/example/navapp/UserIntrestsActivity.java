package com.example.navapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

public class UserIntrestsActivity extends AppCompatActivity {

    private static final String TAG = "UserIntrestsActivity";
    public static MyAppDatabase myAppDatabase;
    private static String allTags="";
    private int count=0;

    List<Tags> tags;
    ArrayList<String> tagNames;
    ArrayList<String> alltagNames;
    JSONArray arr;

    private TextView tvtag;

    private Context context;

    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_tag);

        context=this;

        myAppDatabase= Room.databaseBuilder(getApplicationContext(),MyAppDatabase.class,"tagsdb").allowMainThreadQueries().build();


        tvtag =(TextView)findViewById(R.id.selected_tags);
        listView = (ListView) findViewById(R.id.lvMainFeed);
        //gettags();

        tagNames= new ArrayList<String>();
        alltagNames= new ArrayList<String>();
        arr=new JSONArray();
        getSavedtags();

        List<Tags> tags=myAppDatabase.myDao().getTags();



        for (Tags ourtag:tags)
        {
            String tag_name=ourtag.getTag_name();

            allTags=allTags+"\n"+tag_name;

            count++;


        }

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                List<Tags>intags=myAppDatabase.myDao().getTags();
//
//                try{
//                    listView.getChildAt(position).setEnabled(false);
//
//                    Tags theTag=new Tags();
//                    theTag.setTag_name(alltagNames.get(position));
//
////                    Tags extraTag=new Tags();
////                    // myAppDatabase.myDao().addtag(theTag);
//                  //  List<Tags>intags=myAppDatabase.myDao().getTags();
//                  //  List<Tags>intags=myAppDatabase.myDao().getTags();
////
////                    for (Tags ourtag:intags)
////                    {
////                        if (ourtag.getTag_name().equals(theTag.getTag_name()))
////                            extraTag=ourtag;
////                    }
////
////                    if (!extraTag.equals(null))
////                    {
////                        myAppDatabase.myDao().addtag(theTag);
////                    }
//
//
//
//                   // theTag.setTag_name(alltagNames.get(position));
//
//                    myAppDatabase.myDao().addtag(theTag);
//
//                    intags=myAppDatabase.myDao().getTags();
//                    allTags="";
//                    count=0;
//                    for (Tags ourtag:intags)
//                    {
//                        String tag_name=ourtag.getTag_name();
//
//                        if (count>=4)
//                        {
//                            Toast.makeText(UserIntrestsActivity.this, "You can selectg only four tags", Toast.LENGTH_SHORT).show();
//                            Intent intent=new Intent(context, MainActivity.class);
//                            startActivity(intent);
//                            Log.d(TAG, "checkCurrentUser: no tags found in");
//                        }
//                        else {
//
//                            allTags=allTags+"  "+tag_name;
//                            tvtag.setText(allTags);
//                            count++;
//                        }
//
//
//                        updateMenuItems();
//
//
//                    }
//
//                    Log.d(TAG, "onItemClick: count="+count);
//                }
//                catch (Exception e)
//                {
//                    Log.d(TAG, "onItemClick: "+e.getMessage());
//                }
//
//
//
//
//
//               // tvtag.setText(allTags);
//
//
//
//
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listView.getChildAt(position).setEnabled(false);
                Tags theTag=new Tags();
                theTag.setTag_name(alltagNames.get(position));

                myAppDatabase.myDao().addtag(theTag);
                Log.d(TAG, "onItemClick: added "+theTag.getTag_name());
                List<Tags>intags=myAppDatabase.myDao().getTags();

                allTags="";
                count=0;
                for (Tags ourtag:intags)
                {
                    String tag_name=ourtag.getTag_name();

                    if (count>=4)
                    {
                        Toast.makeText(UserIntrestsActivity.this, "You can selectg only four tags", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(context, MainActivity.class);
                        startActivity(intent);
                        Log.d(TAG, "checkCurrentUser: no tags found in");
                    }
                    else {
                        allTags=allTags+"  "+tag_name;
                        tvtag.setText(allTags);
                        count++;
                    }



                }

                Log.d(TAG, "onItemClick: count="+count);


                // tvtag.setText(allTags);




            }
        });





        new JsonTask().execute("https://api.stackexchange.com/2.2/tags?order=desc&sort=popular&site=stackoverflow");


    }


    private void getSavedtags()
    {
        tags=myAppDatabase.myDao().getTags();




        for (Tags ourtag:tags)
        {
            String tag_name=ourtag.getTag_name();

            if (tag_name!=null)
            {
                tagNames.add(tag_name);

            }


            allTags=allTags+"\n"+tag_name;

            count++;


        }

        Log.d(TAG, "onCreate: "+allTags);

        Log.d(TAG, "onCreate: total items="+count);

        tvtag.setText(allTags);
        Log.d(TAG, "gettags: textview of tags set");


    }

    private void updateMenuItems()
    {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // get menu from navigationView
        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change

        MenuItem tag1 = menu.findItem(R.id.tag1);
        MenuItem tag2 = menu.findItem(R.id.tag2);
        MenuItem tag3 = menu.findItem(R.id.tag3);
        MenuItem tag4 = menu.findItem(R.id.tag4);

//        // set new title to the MenuItem
//        tag1.setTitle("NewTitleForCamera");
//
//        // do the same for other MenuItems
//        MenuItem tag2 = menu.findItem(R.id.tag2);
//        tag2.setTitle("NewTitleForGallery");

        myAppDatabase= Room.databaseBuilder(getApplicationContext(),MyAppDatabase.class,"tagsdb").allowMainThreadQueries().build();
        //  deleteTags();

        List<Tags> tags=myAppDatabase.myDao().getTags();

        try {
            tag1.setTitle(tags.get(1).getTag_name());
            tag2.setTitle(tags.get(1).getTag_name());
            tag3.setTitle(tags.get(2).getTag_name());
            tag4.setTitle(tags.get(3).getTag_name());

        }
        catch (Exception e)
        {
            Log.d(TAG, "onCreate: "+e.getMessage());
        }
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
                String post_id = arr.getJSONObject(i).getString("name");
                //System.out.println(post_id);
                Log.d(TAG, "getTagsFromURL: array "+post_id);
                alltagNames.add(post_id);
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

//               Object content=connection.getContent();
//                try {
//                    JSONArray tags = content.getJSONArray("tags");
//
//                    // List<String> list = new ArrayList<>();
//                    list.clear();
//
//                    for (int i = 0; i < hits.length(); i++)
//                    {
//                        JSONObject jsonObject = hits.getJSONObject(i);
//                        String userNameSuggestions = jsonObject.getString("user_name");
//                        list.add(userNameSuggestions);
//
//                        Log.d(TAG, "requestCompleted: list item "+list.get(0));
//
//                    }
//
//               }
//                catch (JSONException e)
//                {
//                    e.printStackTrace();
//                }




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
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, alltagNames);


            listView.setAdapter(itemsAdapter);
            for (int i = 0; i < arr.length(); i++) {

                Log.d(TAG, "onCreate: all tagnames "+i+" "+alltagNames.get(i));
            }
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

}
