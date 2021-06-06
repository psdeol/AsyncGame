package com.example.asyncgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class GameScreenActivity extends AppCompatActivity {

    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        db = FirebaseDatabase.getInstance().getReference();

        TextView title = findViewById(R.id.text_title);
        Button hintsButton = findViewById(R.id.button_hints);
        Button guessButton = findViewById(R.id.button_guess);
        Button chatButton = findViewById(R.id.button_chat);
        Button backButton = findViewById(R.id.button_back);

        title.setText("Game " + getIntent().getIntExtra("GAMENUM", 0));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void openMessages(View view) {
        Intent intent = new Intent(this, MessagingActivity.class);
        startActivity(intent);
    }
}