package com.example.antonruban.postme.Post;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.antonruban.postme.Database.MyDAO;
import com.example.antonruban.postme.Model.Post;
import com.example.antonruban.postme.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.example.antonruban.postme.HostClass.URL.HOST;
import static com.example.antonruban.postme.MainActivity.list_for;
import static com.example.antonruban.postme.Post.ListPostActivity.list_for_;


public class WritePostActivity extends AppCompatActivity {

    EditText postEdit;
    public static String postWrite;
    public static String log;
    private Context context;

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        postEdit = (EditText) findViewById(R.id.editWritePost);
        context=this;
        Intent intent = getIntent();
        log = intent.getStringExtra("login");
    }
    public void onClickSendPost(View view) throws ExecutionException, InterruptedException {
        postWrite = postEdit.getText().toString();
        if(isOnline())
        {

        SavaPost savaPost = new SavaPost();
        String p=savaPost.execute().get();
        Toast.makeText(this, "Post Send", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(WritePostActivity.this,ListPostActivity.class);
        intent.putExtra("login",log);
        startActivity(intent);
        }
        else{
            Post p = new Post(log,postWrite);
            MyDAO.get(context).addPost(p);
            list_for_.add(p);
            Intent intent = new Intent(WritePostActivity.this,ListPostActivity.class);
            intent.putExtra("login",log);
            startActivity(intent);
            this.finish();
        }

    }
    class SavaPost extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(HOST + "PostList/savePost/"+log+"/"+postWrite);
            HttpResponse response;
            String resp_body = "";
            try {
                response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    resp_body = EntityUtils.toString(response.getEntity());
                    Log.v("SEND_POST", resp_body.toString());
                    Post p = new Post(log,postWrite);
                    MyDAO.get(context).addPost(p);
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

