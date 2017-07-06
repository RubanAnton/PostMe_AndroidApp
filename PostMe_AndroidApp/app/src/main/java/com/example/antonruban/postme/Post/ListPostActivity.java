package com.example.antonruban.postme.Post;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.IntentCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antonruban.postme.Adapter.MyAdapter;
import com.example.antonruban.postme.Database.MyDAO;
import com.example.antonruban.postme.MainActivity;
import com.example.antonruban.postme.Model.ListItem;
import com.example.antonruban.postme.Model.Post;
import com.example.antonruban.postme.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.antonruban.postme.HostClass.URL.HOST;

public class ListPostActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    public static String log;
    private List<Post> post_list;
    public static String POST="",USER="";
    public static Boolean resp_log=false;
    TextView textLogin;
    private Context context;
        public static List<Post> list_for_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_post);
        textLogin  = (TextView) findViewById(R.id.textLoginListPost);
        Intent intent = getIntent();
        context=this;
        log = intent.getStringExtra("login");
        post_list=new ArrayList<Post>();
       // textLogin.setText(log);
        if(list_for_==null) {
            list_for_=new ArrayList<Post>();
        }
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();
        try {
            if(isOnline()) {
                ///////////
                    if(list_for_!=null) {
                        if (!list_for_.isEmpty()) {
                            List<Post> for_delete = new ArrayList<Post>();
                            for_delete = list_for_;
                            Toast.makeText(context, "Synchronizing ...", Toast.LENGTH_LONG).show();
                            for (Post us : list_for_) {

                                USER = us.getUsername_();
                                POST = us.getPost_();

                                SavaPost st = new SavaPost();
                                try {
                                    String s = st.execute().get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                            list_for_.clear();
                        }
                    }
                ///////////
            String my = new GetPost().execute().get();
            //Log.d("THIS IS TASK ", my);
            if (my != null) {
                JSONArray jsonarray = new JSONArray(my);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String Login = jsonobject.getString("login");
                    String Post = jsonobject.getString("postText");
                    ListItem listItem = new ListItem(Login, Post);
                    listItems.add(listItem);
                    //Log.d("This is login ", Login + " " + Post);
                }
            }

            }else {
                post_list= MyDAO.get(context).getPosts();
                for (Post item: post_list
                        ) {
                    ListItem listItem = new ListItem(item.getUsername_(), item.getPost_());
                    listItems.add(listItem);
                }
            }/// ТУТ ПОСТЫ ЕСЛИ ОФЛАЙН
            adapter = new MyAdapter(listItems,this);
            recyclerView.setAdapter(adapter);
            //listItems.clear();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


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
        getMenuInflater().inflate(R.menu.list_post, menu);
        return true;
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            if(isOnline()){
                Intent intent = new Intent(ListPostActivity.this, ListPostActivity.class);
                intent.putExtra("login", log);
                startActivity(intent);
                this.finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.WritePost) {

                Intent intent = new Intent(ListPostActivity.this, WritePostActivity.class);
                intent.putExtra("login", log);
                startActivity(intent);


        } else if (id == R.id.UserPostList) {
            if(isOnline()) {
                Intent intent = new Intent(ListPostActivity.this, UserPostListActivity.class);
                intent.putExtra("login", log);
                startActivity(intent);
            }

        } else if(id == R.id.AllUser){
            if(isOnline()) {
                Intent intent = new Intent(ListPostActivity.this, AllUsersActivity.class);
                intent.putExtra("login", log);
                startActivity(intent);
            }
        }   else if(id == R.id.FollowingUser){
            if(isOnline()) {
                Intent intent = new Intent(ListPostActivity.this, FollowingUserActivity.class);
                intent.putExtra("login", log);
                startActivity(intent);
            }
        } else if(id == R.id.FollowerUser){
            if(isOnline()) {
                Intent intent = new Intent(ListPostActivity.this, FollowerUserActivity.class);
                intent.putExtra("login", log);
                startActivity(intent);
            }
        } else if(id == R.id.Exit){

                Intent intent = new Intent(ListPostActivity.this, MainActivity.class);
                ComponentName cn = intent.getComponent();
                Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                startActivity(mainIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class GetPost extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(HOST + "PostList/post");
            HttpResponse response;
            String resp_body = "";
            try {
                response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    resp_body = EntityUtils.toString(response.getEntity());
                    resp_log=Boolean.valueOf(resp_body.toString());
                }
                return resp_body;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

    }
    class SavaPost extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(HOST + "PostList/savePost/"+USER+"/"+POST);
            HttpResponse response;
            String resp_body = "";
            try {
                response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    resp_body = EntityUtils.toString(response.getEntity());
                    Log.v("SEND_POST", resp_body.toString());
                    Post p = new Post(USER,POST);
                    //MyDAO.get(context).addPost(p);
                    //resp_log=Boolean.valueOf(resp_body.toString());

                }
                return resp_body;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }
}
