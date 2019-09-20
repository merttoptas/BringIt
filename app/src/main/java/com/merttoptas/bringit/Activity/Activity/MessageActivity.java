package com.merttoptas.bringit.Activity.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.merttoptas.bringit.Activity.Fragment.MapsFragment;
import com.merttoptas.bringit.Activity.Fragment.MessageFragment;
import com.merttoptas.bringit.R;

public class MessageActivity extends AppCompatActivity {

    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    ImageButton btn_send;
    EditText etMessageSend;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        btn_send = findViewById(R.id.btn_send);
        etMessageSend = findViewById(R.id.etMessageSend);
        recyclerView = findViewById(R.id.recyclerview);

        ref = FirebaseDatabase.getInstance().getReference("chats");


    }
}
