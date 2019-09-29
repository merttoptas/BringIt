package com.merttoptas.bringit.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;
import com.merttoptas.bringit.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private List<User> mUsers;

    public UserAdapter(Context mContext, List<User> mUsers){
        this.mContext=mContext;
        this.mUsers = mUsers;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent,false);

        return new  UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = mUsers.get(position);
        final int viewType = holder.getItemViewType();

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.user_image);
        }
    }
}
