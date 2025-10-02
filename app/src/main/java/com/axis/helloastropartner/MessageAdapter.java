package com.axis.helloastropartner;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private List<Message> messageList;
    private String currentUserId;

    public MessageAdapter(List<Message> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }
    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        Message msg = messageList.get(position);

        holder.messageText.setText(msg.getMessage());

        // Different alignment for sent vs received
        if (msg.getSenderId().equals(currentUserId)) {
            holder.messageText.setBackgroundResource(R.drawable.bg_message_sent);
            ((LinearLayout.LayoutParams) holder.messageText.getLayoutParams()).gravity = Gravity.END;
        } else {
            holder.messageText.setBackgroundResource(R.drawable.bg_message_received);
            ((LinearLayout.LayoutParams) holder.messageText.getLayoutParams()).gravity = Gravity.START;
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
        }
    }
}
