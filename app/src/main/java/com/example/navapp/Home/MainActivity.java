package com.example.navapp.Home;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.navapp.MyAppDatabase;
import com.example.navapp.R;
import com.example.navapp.SectionPagerAdapter;
import com.example.navapp.Tags;
import com.example.navapp.UserIntrestsActivity;
import com.example.navapp.WeekFragment;
import com.example.navapp.YourFragment;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    Menu menu;
    private ViewPager mViewPager;

    private Context mContext=MainActivity.this;

    public static MyAppDatabase myAppDatabase;
    private int count=0;

    private static String allTags="";

    public static int menuitem=0;

    public int getMenuitem() {
        return menuitem;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager=(ViewPager) findViewById(R.id.viewpager_container);

        setUpViewpager();



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
        //deleteTags();
        checktags();
        List<Tags> tags=myAppDatabase.myDao().getTags();

        try {
            tag1.setTitle(tags.get(0).getTag_name());
            tag2.setTitle(tags.get(1).getTag_name());
            tag3.setTitle(tags.get(2).getTag_name());
            tag4.setTitle(tags.get(3).getTag_name());

        }
        catch (Exception e)
        {
            Log.d(TAG, "onCreate: "+e.getMessage());
        }




        // add NavigationItemSelectedListener to check the navigation clicks
        navigationView.setNavigationItemSelectedListener(this);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.tag1) {
            // Handle the camera action
            item.setChecked(true);

            menuitem=0;
            Log.d(TAG, "onNavigationItemSelected: "+menuitem);

            setUpViewpager();

        } else if (id == R.id.tag2) {
            item.setChecked(true);
            menuitem=1;
           // YourFragment.updateList(menuitem);
            Log.d(TAG, "onNavigationItemSelected: "+menuitem);
          setUpViewpager();


        } else if (id == R.id.tag3) {
            item.setChecked(true);
            menuitem=2;
          //  YourFragment.updateList(menuitem);
            Log.d(TAG, "onNavigationItemSelected: "+menuitem);
            setUpViewpager();

        } else if (id == R.id.tag4) {
            item.setChecked(true);
            menuitem=3;
          //  YourFragment.updateList(menuitem);
            Log.d(TAG, "onNavigationItemSelected: "+menuitem);
            setUpViewpager();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



//    public int NavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.tag1) {
//            // Handle the camera action
//            item.setChecked(true);
//            return 0;
//        } else if (id == R.id.tag2) {
//            item.setChecked(true);
//            return 1;
//        } else if (id == R.id.tag3) {
//            item.setChecked(true);
//            return 2;
//
//        } else if (id == R.id.tag4) {
//            item.setChecked(true);
//            return 3;
//        }
//
//        return -1;
//
//
//    }



    //Responsible for adding three tabs camera,home,messages
    private void setUpViewpager()
    {
        SectionPagerAdapter adapter=new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new YourFragment());
        adapter.addFragment(new HotFragment());
        adapter.addFragment(new WeekFragment());

        mViewPager.setAdapter(adapter);

        TabLayout tabLayout=(TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText("Your#");
        tabLayout.getTabAt(1).setText("Hot");
        tabLayout.getTabAt(2).setText("Week");

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



    private void checktags()
    {
        List<Tags> tags=myAppDatabase.myDao().getTags();



        for (Tags ourtag:tags)
        {
            String tag_name=ourtag.getTag_name();

            allTags=allTags+"  "+tag_name;

            count++;


        }

        Log.d(TAG, "onCreate: "+allTags);

        Log.d(TAG, "onCreate: total items="+count);

        if (count<4)
        {
            Intent intent=new Intent(mContext, UserIntrestsActivity.class);
            startActivity(intent);
            Log.d(TAG, "checkCurrentUser: no tags found in");
        }

    }


    private void deleteTags()
    {

        List<Tags> tags=myAppDatabase.myDao().getTags();

        //  Delete Tags
        for (Tags ourtag:tags)
        {
            myAppDatabase.myDao().deleteTag(ourtag);


        }

    }


    public static class HotFragment extends Fragment {

        private static final String TAG = "HotFragment";
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
            View view=inflater.inflate(R.layout.fragment_week,container,false);


            allques= new ArrayList<String>();
            allquesLink= new ArrayList<String>();
            arr=new JSONArray();
            map = new HashMap<String, String>();
            webView=(WebView) view.findViewById(R.id.webView);

            listView = (ListView) view.findViewById(R.id.lvWeek);

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


            String u="https://api.stackexchange.com/2.2/questions/featured?order=desc&sort=activity&tagged=java&site=stackoverflow";
            try{


                String path1="https://api.stackexchange.com/2.2/questions/featured?order=desc&sort=activity&tagged=";
                String tagpath=tags.get(getCheckedItem()).getTag_name();
                String path2="&site=stackoverflow";
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
                Log.d(TAG, "onPostExecute: list view set for hot");
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

            int i = menuitem;
            return i;
        }

    }
}
