package com.example.antonruban.postme;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.antonruban.postme.Database.MyDAO;
import com.example.antonruban.postme.Model.User;
import com.example.antonruban.postme.Post.ListPostActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.antonruban.postme.HostClass.URL.HOST;

public class MainActivity extends AppCompatActivity {
        public static String SHAHash;
        private String username;
        private String password;

    private Context context;
        public static  boolean response_log;
        public static JSONObject jsobj;
        public static String str,pass;
    private User mUser;
    public static List<User> list_for;
        // MyDBHelper dbHelper = new MyDBHelper(this);
        //SQLiteDatabase db;
        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            context = this;
            final EditText userName = (EditText) findViewById(R.id.loginMain);
            final EditText passwordMain = (EditText) findViewById(R.id.passwordMain);
            //singUp
            Button singUp = (Button) findViewById(R.id.btnSignUp);
            singUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isOnline()) {
                        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                        startActivity(intent);
                    }
                }
            });

            //logIn
            Button logIn = (Button) findViewById(R.id.btnLogIn);
            logIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    username = userName.getText().toString();
                    password = passwordMain.getText().toString();
                    mUser = MyDAO.get(context).getUser(username);
                    if(isOnline()) {
                        try {

                            CheckUser mt = new CheckUser();
                            str = userName.getText().toString().trim();
                            pass = passwordMain.getText().toString();
                            mt.execute();
                            try {
                                String str_result = new CheckUser().execute().get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            if (response_log ) {

                                Intent intent = new Intent(MainActivity.this, ListPostActivity.class);
                                intent.putExtra("login", username);
                                startActivity(intent);

                            }
                            else {
                                Toast.makeText(context, "No such user", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                        }

                    }
                    else{
                        username = userName.getText().toString();
                        password = passwordMain.getText().toString();
                        mUser = MyDAO.get(context).getUser(username);
                        if(mUser != null && mUser.getPassword().equals(password))
                        {

                            Toast.makeText(context, "Ofline", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, ListPostActivity.class);
                            intent.putExtra("login", username);
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(context, "Ofline. Doesn't exists in local db.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        class CheckUser extends AsyncTask<Void,Void,String> {
            @Override
            protected String doInBackground(Void... params) {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(HOST+"UC/login/"+str+"/"+pass);
                HttpResponse response;
                try {
                    response = client.execute(request);
                    if(response.getStatusLine().getStatusCode()==200) {
                        try {
                            String resp_body = EntityUtils.toString(response.getEntity());
                            Log.v("resp_body", resp_body.toString());
                            response_log=Boolean.valueOf(resp_body.toString());

                        }
                        catch(Exception e)
                        {

                            Log.e("sometag",e.getMessage());
                        }
                    }
                    return response.toString();
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace(); return null;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();return null;
                }
            }
        }
}


