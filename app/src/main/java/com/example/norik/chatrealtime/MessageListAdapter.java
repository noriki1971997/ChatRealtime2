package com.example.norik.chatrealtime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<item_message_list_data> message_list_data;
    private String currentName;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    public MessageListAdapter(Context context, ArrayList<item_message_list_data> message_list_data,String currentName) {
        this.context = context;
        this.message_list_data = message_list_data;
        this.currentName = currentName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (i == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_message_sent, viewGroup, false);
            return new SentMessageHolder(view);
        } else if (i == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_message_received, viewGroup, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        item_message_list_data message =  message_list_data.get(i);

        switch (viewHolder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) viewHolder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) viewHolder).bind(message);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        item_message_list_data message = message_list_data.get(position);
        if (message.getName().equals(currentName)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }



    @Override
    public int getItemCount() {
        return message_list_data.size();
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.tvSentMessageContent);

        }

        void bind(item_message_list_data message) {
            messageText.setText(message.getMessage());

        }
    }


    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, nameText;
        ImageView profileImage;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.tvReceivedMessageContent);
            nameText = itemView.findViewById(R.id.tvName);
            profileImage = itemView.findViewById(R.id.imgview_avatar);
        }
        void bind(item_message_list_data message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.

            nameText.setText(message.getName());

            // Insert the profile image from the URL into the ImageView.
            profileImage.setImageResource(R.drawable.happy);
        }
    }
}
