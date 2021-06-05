package com.example.asyncgame;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private DatabaseReference db;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button game1TurnButton = view.findViewById(R.id.button_turn_1);
        Button game2TurnButton = view.findViewById(R.id.button_turn_2);
        Button game1WaitButton = view.findViewById(R.id.button_wait_1);
        Button game2WaitButton = view.findViewById(R.id.button_wait_2);

        db = FirebaseDatabase.getInstance().getReference();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                DataSnapshot ds = snapshot.getChildren().iterator().next();
                String gameStatus = ds.child("game1").child("gameStatus").getValue().toString();

                if (gameStatus.equals("hints")) {
                    game1TurnButton.setVisibility(View.VISIBLE);
                } else {
                    game1WaitButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
                Log.w("ERROR", "loadPost:onCancelled", error.toException());
            }
        };

        db.addValueEventListener(postListener);

        Button newGameButton = view.findViewById(R.id.button_new_game);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Max Number of Games Reached", Toast.LENGTH_SHORT).show();
            }
        });

        game1TurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), GameScreenActivity.class);
                intent.putExtra("GAMENUM", 1);
                startActivity(intent);
            }
        });

        game2WaitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Waiting For Other Players", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}