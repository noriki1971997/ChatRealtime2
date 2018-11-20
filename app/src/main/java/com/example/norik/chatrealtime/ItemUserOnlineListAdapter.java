package com.example.norik.chatrealtime;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemUserOnlineListAdapter extends RecyclerView.Adapter<ItemUserOnlineListAdapter.ViewHolder>{
    ArrayList<RowItemsUserOnlineData> dataArrayList;
    Context context;

    public ItemUserOnlineListAdapter(ArrayList<RowItemsUserOnlineData> dataArrayList, Context context) {
        this.dataArrayList = dataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.row_items_usersonline_list,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.txtusername.setText(dataArrayList.get(i).getUsername());
        viewHolder.imgstatus.setImageResource(dataArrayList.get(i).getStatus());
        viewHolder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (onItemClickedListener != null) {
                    onItemClickedListener.onItemClick(viewHolder.txtusername.getText().toString());
                }
            }
        });
    }

    public interface OnItemClickedListener {
        void onItemClick(String username);
    }

    private OnItemClickedListener onItemClickedListener;
    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtusername;
        ImageView imgstatus;
        ConstraintLayout line;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtusername = itemView.findViewById(R.id.tvUsenameOnline);
            imgstatus = itemView.findViewById(R.id.imgviewDotGreen);
            line = itemView.findViewById(R.id.lineOfUserOnlineList);
        }
    }
}
