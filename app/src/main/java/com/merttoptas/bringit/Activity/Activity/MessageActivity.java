package com.merttoptas.bringit.Activity.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.merttoptas.bringit.Activity.Adapter.MessageAdapter;
import com.merttoptas.bringit.Activity.Fragment.ApiService;
import com.merttoptas.bringit.Activity.Model.Chat;
import com.merttoptas.bringit.Activity.Model.User;
import com.merttoptas.bringit.Activity.Notifications.Client;
import com.merttoptas.bringit.Activity.Notifications.Data;
import com.merttoptas.bringit.Activity.Notifications.MyResponse;
import com.merttoptas.bringit.Activity.Notifications.Sender;
import com.merttoptas.bringit.Activity.Notifications.Token;
import com.merttoptas.bringit.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference ref;
    ImageButton btn_send;
    EditText etMessageSend;
    RecyclerView recyclerView;
    SharedPreferences myPrefs;
    Intent intent;
    FirebaseUser firebaseUser;
    MessageAdapter messageAdapter;
    List<Chat> mChat = new ArrayList<>();
    private List<User> mUsers;
    CircleImageView profile_image;
    TextView username;
    ValueEventListener seenListener;
    ApiService apiService;
    String userid;
    boolean notify = false;



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

        bindViews();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(ApiService.class);

        intent = getIntent();
        userid = intent.getStringExtra("userid");
        //Firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        // recyclerview  set.
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        getDetailsInfo();
        readUserInfo();

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(), DetailActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            overridePendingTransition (0, 0);
            startActivity(i);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void seenMessage(final String userid){
        ref = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat =  snapshot.getValue(Chat.class);

                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)){

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendMessage(String sender, final String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);

        reference.child("Chats").push().setValue(hashMap);


        // add user to chat fragment
        intent = getIntent();
        if(intent.hasExtra("userid")){
            userid = intent.getStringExtra("userid");

            final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                    .child(firebaseUser.getUid())
                    .child(userid);

            chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()){
                        chatRef.child("id").setValue(userid);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                    .child(userid)
                    .child(firebaseUser.getUid());
            chatRefReceiver.child("id").setValue(firebaseUser.getUid());

        }
        if(intent.hasExtra("useridRw1")){
            final String userid1 = getIntent().getStringExtra("useridRw1");

            final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                    .child(firebaseUser.getUid())
                    .child(userid1);

            chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()){
                        chatRef.child("id").setValue(userid1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                    .child(userid1)
                    .child(firebaseUser.getUid());
            chatRefReceiver.child("id").setValue(firebaseUser.getUid());
        }



        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {
                    sendNotifiaction(receiver, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotifiaction(String receiver, final String username, final String message) {


        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");

        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.mipmap.ic_launcher, username+": " + message, getString(R.string.yeni_mesaj), userid);
                    if(intent.hasExtra("useridRw1")){
                        final String userid1 = getIntent().getStringExtra("useridRw1");
                        Data data1 = new Data(firebaseUser.getUid(), R.mipmap.ic_launcher, username+": " + message, getString(R.string.yeni_mesaj), userid1);
                        Sender sender = new Sender (data1, token.getToken());
                        apiService.sendNotification(sender)
                                .enqueue(new Callback<MyResponse>() {
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                        if(response.code() ==200){
                                            if(response.body().success ==1){
                                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT);

                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MyResponse> call, Throwable t) {

                                    }
                                });
                    }
                    Sender sender = new Sender (data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() ==200){
                                        if(response.body().success ==1){
                                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT);

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void btnSend(View view) {
        intent =getIntent();
        notify =true;
        if (intent.hasExtra("userid")){
            final String userid = intent.getStringExtra("userid");
            String msg = etMessageSend.getText().toString();
            if(!msg.equals("")){
                sendMessage(firebaseUser.getUid(), userid, msg);
            }else{
                Toast.makeText(getApplicationContext(), "You can't send empty message", Toast.LENGTH_SHORT).show();
            }
            etMessageSend.setText("");
        }
        if (intent.hasExtra("useridRw1")){

            final String userid1 = getIntent().getStringExtra("useridRw1");
            String msg = etMessageSend.getText().toString();
            if(!msg.equals("")){
                sendMessage(firebaseUser.getUid(), userid1, msg);
            }else{
                Toast.makeText(getApplicationContext(), "You can't send empty message", Toast.LENGTH_SHORT).show();
            }
            etMessageSend.setText("");
        }
    }

    private void readMessages(final String myid, final String userid){

        mChat =new ArrayList<>();

        ref =FirebaseDatabase.getInstance().getReference("Chats");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);

                    assert chat != null;
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid))
                    {
                    mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this,mChat);
                    recyclerView.setAdapter(messageAdapter);
                    messageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void bindViews(){

        btn_send = findViewById(R.id.btn_send);
        etMessageSend = findViewById(R.id.etMessageSend);
        recyclerView = findViewById(R.id.mRecyclerview);
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
    }

    private void readUserInfo(){

        intent =getIntent();

        if(intent.hasExtra("userid")){
            final String userid = intent.getStringExtra("userid");
            assert userid != null;
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);
                    assert user != null;
                    username.setText(user.getUsername());
                    Glide.with(getApplicationContext()).load(Uri.parse(user.getImageURL())).into(profile_image);
                    readMessages(firebaseUser.getUid(), userid);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            seenMessage(userid);

        }

    }
    private void getDetailsInfo(){

        intent =getIntent();
        if (intent.hasExtra("useridRw1")){
            final String userid = getIntent().getStringExtra("useridRw1");
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
            // Users in position get image photo
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot:  dataSnapshot.getChildren()){

                        User user = snapshot.getValue(User.class);
                        assert user != null;
                        if(user.getId().equals(userid)){

                            String imageUrl = user.getImageURL();
                            username.setText(user.getUsername());
                            Glide.with(getApplicationContext()).load(imageUrl).into(profile_image);

                        }
                        readMessages(firebaseUser.getUid(), userid);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void status(String status){
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        ref.updateChildren(hashMap);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(seenListener !=null){
            ref.removeEventListener(seenListener);
        }
        status("offline");
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");

    }
}
