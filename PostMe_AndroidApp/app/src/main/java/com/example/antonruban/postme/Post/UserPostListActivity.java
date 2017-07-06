package com.example.antonruban.postme.Post;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.antonruban.postme.Adapter.MyAdapter;
import com.example.antonruban.postme.Model.ListItem;
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


public class UserPostListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    public static String log;
    public static Boolean resp_log=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post_list);

        Intent intent = getIntent();
        log = intent.getStringExtra("login");
        recyclerView = (RecyclerView) findViewById(R.id.recycleUserList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();
        try {
            String my = new GetPost().execute().get();
            //Log.d("THIS IS TASK ", my);
            if (my != null) {
                JSONArray jsonarray = new JSONArray(my);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String Login = jsonobject.getString("login");
                    String Post = jsonobject.getString("postText");
                    ListItem listItem = new ListItem(Login,Post);
                    listItems.add(listItem);
                    //Log.d("This is login ", Login + " " + Post);
                }
                adapter = new MyAdapter(listItems,this);
                recyclerView.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    class GetPost extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(HOST + "PostList/postUser/" + log);
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
}