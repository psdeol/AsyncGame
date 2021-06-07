package com.example.asyncgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
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


        /*
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                SharedPreferences sharedPref = requireContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
                String guessOrHint = sharedPref.getString("nextTurn", "hints");
                String gameStatus = snapshot.child("games").child("game1").child("gameStatus").getValue().toString();

                Log.d("DEBUG", guessOrHint + "---" + gameStatus);
                if(guessOrHint.equals(gameStatus)) {
                    Log.d("DEBUG", "New turn! Can now perform " + gameStatus);
                    game1WaitButton.setVisibility(View.GONE);
                    game1TurnButton.setVisibility(View.VISIBLE);
                }
                else {
                    Log.d("DEBUG", "still need to wait for everyone to complete their turn");
                    game2TurnButton.setVisibility(View.GONE);
                    game1WaitButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
                Log.w("ERROR", "loadPost:onCancelled", error.toException());
            }
        };

        db.addValueEventListener(postListener);


         */
        game1TurnButton.setVisibility(View.VISIBLE);
        game2WaitButton.setVisibility(View.VISIBLE);

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