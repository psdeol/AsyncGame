package com.example.asyncgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.DataEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class MessagingActivity extends AppCompatActivity {

    private DatabaseReference db;
    private Intent intent;
    public String gameID = "testGame";
    public RecyclerView recyclerView;
    public MessageAdapter messageAdapter;
    public ArrayList mchat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        gameID = "game" + getIntent().getIntExtra("GAMENUM", 0);

        db = FirebaseDatabase.getInstance().getReference("messages/" + gameID);
        recyclerView = findViewById(R.id.recycler);

        mchat = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        db.orderByChild("datetime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchat.clear();
                for (DataSnapshot dss : snapshot.getChildren()) {
                    Chat chat = dss.getValue(Chat.class);
                    mchat.add(chat);
                    messageAdapter = new MessageAdapter(MessagingActivity.this, mchat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void sendMessage(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
        String datetime = sdf.format(new Date());

        String author = FirebaseAuth.getInstance().getCurrentUser().getUid();

        EditText sendText = findViewById(R.id.sendText);
        String content = sendText.getText().toString();

        db.push().setValue(new Chat(datetime, author, content));
        sendText.setText("");
    }

}