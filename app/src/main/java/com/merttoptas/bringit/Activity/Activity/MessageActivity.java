package com.merttoptas.bringit.Activity.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.merttoptas.bringit.Activity.Adapter.MessageAdapter;
import com.merttoptas.bringit.Activity.Model.Chat;
import com.merttoptas.bringit.Activity.Model.User;
import com.merttoptas.bringit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MessageActivity extends AppCompatActivity {

    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    ImageButton btn_send;
    EditText etMessageSend;
    RecyclerView recyclerView;
    SharedPreferences myPrefs;
    Intent intent;
    FirebaseUser firebaseUser;
    MessageAdapter messageAdapter;
    List<Chat> mChat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){

            setTheme(R.style.darktheme);
        }else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_message);

        myPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        getDelegate().setLocalNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        btn_send = findViewById(R.id.btn_send);
        etMessageSend = findViewById(R.id.etMessageSend);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /*
        String userid = intent.getStringExtra("userid");

        ref = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
         */



        intent = getIntent();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){

            startActivity(new Intent(getApplicationContext(), DetailActivity.class));
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void  sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }

    public void btnSend(View view) {
        String msg = etMessageSend.getText().toString();

        if(!msg.equals("")){
            sendMessage(firebaseUser.getUid(), firebaseUser.getDisplayName(), msg);
        }else{
            Toast.makeText(getApplicationContext(), "You can't send empty message", Toast.LENGTH_SHORT).show();
        }
        etMessageSend.setText("");
    }

    private void readMessages(final String myid, final String userid, final String imageurl){
        mChat =new ArrayList<>();

        ref =FirebaseDatabase.getInstance().getReference("Chats");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid))
                    {
                    mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
