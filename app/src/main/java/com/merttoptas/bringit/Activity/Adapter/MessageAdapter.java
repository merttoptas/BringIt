package com.merttoptas.bringit.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.merttoptas.bringit.Activity.Model.Chat;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.merttoptas.bringit.R;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private Context mContext;
    private List<Chat> mChat;
    public static final int MSG_TITLE_LEFT =0;
    public static final int MSG_TITLE_RIGHT=1;
    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext, List<Chat> mChat){
        this.mContext=mContext;
        this.mChat = mChat;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType ==MSG_TITLE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);

        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat =mChat.get(position);

        holder.show_message.setText(chat.getMessage());

    }


    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView show_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
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
