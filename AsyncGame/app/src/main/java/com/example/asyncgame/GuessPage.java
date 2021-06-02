package com.example.asyncgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    String myCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_page);
        hintDisplay = findViewById(R.id.guessList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        hintDisplay.setLayoutManager(layoutManager);
        myAdapter = new GuessAdapter(allHints);
        hintDisplay.setAdapter(myAdapter);
        getDummyData();
    }


    public void onSubmitButton(android.view.View myView) {
        EditText guessText = findViewById(R.id.actualText);
        String enteredText = guessText.getText().toString();
        if(this.getCurrentFocus() != null) {
            Context context = this.getApplicationContext();
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        Log.d("myDebug", "entered text is: " + enteredText);
        if (myCard != null  &&  enteredText != null  &&  enteredText.toLowerCase().equals(myCard.toLowerCase())) {
            Toast.makeText(getApplicationContext(), "Correct guess!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Incorrect guess :(", Toast.LENGTH_LONG).show();
        }

        finish();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }


    public void onSkipButton(android.view.View myView) {
        Toast.makeText(getApplicationContext(), "Skipping guess", Toast.LENGTH_LONG).show();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }


    public void getDummyData() {
        DatabaseReference emaRef = FirebaseDatabase.getInstance().getReference("hintsTest");
        emaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myCard = (String) snapshot.child("currentCard").getValue();
                Log.d("myDebug", "my card is: " + myCard);
                DataSnapshot hintSnapshot = snapshot.child("hints");
                for (DataSnapshot snap : hintSnapshot.getChildren()) {
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

    public void createTestData() {
        DatabaseReference emaRef = FirebaseDatabase.getInstance().getReference("hintsTest");
        HintInfo[] hints = {new HintInfo("hint1", "Noah"), new HintInfo("hint2", "Prab"),
        new HintInfo("hint3", "Sam"), new HintInfo("hint4", "Ben"),
                new HintInfo("hint5", "Billy") };
        emaRef.child("hints").setValue(Arrays.asList(hints));
        emaRef.child("currentCard").setValue("tempCard");
    }


    class HintInfo {
        public String hint;
        public String user;

        public HintInfo(String hint, String user) {
            this.hint = hint;
            this.user = user;
        }

    }



}