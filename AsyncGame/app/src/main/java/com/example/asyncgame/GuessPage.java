package com.example.asyncgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.installations.FirebaseInstallations;

import java.util.ArrayList;
import java.util.Arrays;

public class GuessPage extends AppCompatActivity {

    private ArrayList<HintInfo> allHints = new ArrayList<>();
    RecyclerView hintDisplay;
    GuessAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_page);
        hintDisplay = findViewById(R.id.guessList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        hintDisplay.setLayoutManager(manager);
        myAdapter = new GuessAdapter(allHints);
        hintDisplay.setAdapter(myAdapter);
        getDummyData();
    }

    public void getDummyData() {
        DatabaseReference emaRef = FirebaseDatabase.getInstance().getReference("hintsTest");
        emaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String hint = (String) snap.child("hint").getValue();
                    String name = (String) snap.child("user").getValue();
                    HintInfo hInfo = new HintInfo(hint, name);
                    allHints.add(hInfo);
                    Log.d("myDebug", "hint is: " + hint + " and name is: " + name);
                }
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*public void createTestData() {
        DatabaseReference emaRef = FirebaseDatabase.getInstance().getReference("hintsTest");
        HintInfo[] hints = {new HintInfo("hint1", "Noah"), new HintInfo("hint2", "Prab"),
        new HintInfo("hint3", "Sam"), new HintInfo("hint4", "Ben"),
                new HintInfo("hint5", "Billy") };
        emaRef.setValue(Arrays.asList(hints));
    }*/


    class HintInfo {
        public String hint;
        public String user;

        public HintInfo(String hint, String user) {
            this.hint = hint;
            this.user = user;
        }

    }



}