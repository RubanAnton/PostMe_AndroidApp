package com.example.antonruban.postme.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.antonruban.postme.Model.ListItem;
import com.example.antonruban.postme.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;

    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);

        holder.loginHeading.setText(listItem.getLoginHead());
        holder.postDesc_.setText(listItem.getPostDesc());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView loginHeading;
        public TextView postDesc_;


        public ViewHolder(View itemView) {
            super(itemView);


            loginHeading = (TextView) itemView.findViewById(R.id.loginHead);
            postDesc_ = (TextView) itemView.findViewById(R.id.postDesc);

        }
    }
}
