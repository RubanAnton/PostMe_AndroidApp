package com.example.antonruban.postme.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.antonruban.postme.Model.ListFollow;
import com.example.antonruban.postme.R;

import java.util.List;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    private List<ListFollow> listItems;
    private Context context;

    public FollowAdapter(List<ListFollow> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListFollow listItem = listItems.get(position);

        holder.loginHeading.setText(listItem.getLoginFollowList());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView loginHeading;



        public ViewHolder(View itemView) {
            super(itemView);
            loginHeading = (TextView) itemView.findViewById(R.id.loginFollowList);


        }
    }
}
