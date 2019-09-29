package com.merttoptas.bringit.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.merttoptas.bringit.Activity.Model.Chat;
import com.merttoptas.bringit.R;

import java.util.List;

public class MessageAdapter extends  RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public static final int MSG_TITLE_LEFT =0;
    public static final int MSG_TITLE_RIGHT=1;
    private Context mContext;
    private List<Chat> mChat;
    private String imageUrl;
    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageUrl) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageUrl =imageUrl;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType ==MSG_TITLE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.MessageViewHolder(view);

        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.MessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        final int viewType =holder.getItemViewType();

        Chat chat =mChat.get(position);

        holder.show_message.setText(chat.getMessage());

        if (imageUrl.equals("default")){

            holder.profile_image.setImageResource(R.drawable.userphoto);
        }else{
            Glide.with(mContext).load(imageUrl).into(holder.profile_image);
        }
    }


    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_image;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image =itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TITLE_RIGHT;
        }else {
            return MSG_TITLE_LEFT;
        }

    }
}
