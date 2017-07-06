package com.example.antonruban.postme.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antonruban.postme.Model.ListUserItem;
import com.example.antonruban.postme.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.antonruban.postme.HostClass.URL.HOST;
import static com.example.antonruban.postme.Post.ListPostActivity.log;


public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.ViewHolder> {
    private List<ListUserItem> listUserItems;
    private Context context;
    public static String dest_us = null,dest_us2=null;
    public static boolean response_log=false;
    public AllUserAdapter(List<ListUserItem> listUserItems, Context context) {
        this.listUserItems = listUserItems;
        this.context = context;
    }

    @Override
    public AllUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_alluser_item, parent, false);
        return new AllUserAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AllUserAdapter.ViewHolder holder, int position) {
        final ListUserItem listItem = listUserItems.get(position);
        dest_us = listItem.getLogin();
        holder.loginAllUser.setText(listItem.getLogin());
        try {
            String mt2 = new MyTask().execute().get();
            if(response_log){ holder.btnFollow.setText("FOOLOWED");
                holder.btnFollow.setTextColor(Color.BLUE);}
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dest_us=log;
                dest_us2 = listItem.getLogin();
                dest_us =listItem.getLogin();try {
                    String mt2 = new MyTask().execute().get();
                    if(response_log){ holder.btnFollow.setText("FOOLOWED");
                        holder.btnFollow.setTextColor(Color.BLUE);}
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                try {
                    if (dest_us2.equals(log)  ) {
                        Toast.makeText(context, "You can't follow yourself", Toast.LENGTH_SHORT).show();
                    } else {
                        if(response_log==false)
                        {String mt = new AddFollower().execute().get();}
                        else {
                            Toast.makeText(context, "Already followed", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (!dest_us2.equals(log)) {
                    holder.btnFollow.setText("FOLLOWED");
                    holder.btnFollow.setTextColor(Color.BLUE);
                }

                //post-to add user
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUserItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView loginAllUser;
        public Button btnFollow;

        public ViewHolder(View itemView) {
            super(itemView);

            btnFollow = (Button) itemView.findViewById(R.id.btnFollowUserAllList);
            loginAllUser = (TextView) itemView.findViewById(R.id.loginUserAllList);
        }
    }

    class AddFollower extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("follower", log);
                jsonObject.accumulate("following", dest_us2);
                String data = jsonObject.toString();
                Log.d("json data", data);
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(HOST + "Follow/follower");
                StringEntity se = new StringEntity(data);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                Log.d("----!!!!_----", httpResponse.getEntity().getContent().toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    class MyTask extends AsyncTask<Void,Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(HOST+"Follow/isFollowed/"+log+"/"+dest_us);
            HttpResponse response;
            try {
                response = client.execute(request);
                if(response.getStatusLine().getStatusCode()==200)
                {
                    try
                    {
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

