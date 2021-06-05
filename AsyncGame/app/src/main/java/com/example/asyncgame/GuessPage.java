package com.example.asyncgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GuessPage extends AppCompatActivity {

    private ArrayList<HintInfo> allHints = new ArrayList<>();
    RecyclerView hintDisplay;
    GuessAdapter myAdapter;
    String myCard;
    String myEmail = "user-prab@email.com";
    ArrayList<String> cards = new ArrayList<String>();
    int cardInd;
    private HashMap<String, String> emailToPlayer = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_page);
        hintDisplay = findViewById(R.id.guessList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        hintDisplay.setLayoutManager(layoutManager);
        myAdapter = new GuessAdapter(allHints);
        hintDisplay.setAdapter(myAdapter);
        emailToPlayer.put("email1", "player1");
        emailToPlayer.put("email2", "player2");
        emailToPlayer.put("email3", "player3");
        listenForNextTurn();
        getData();
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
        boolean correctGuess = false;
        if (myCard != null  &&  enteredText != null  &&  enteredText.toLowerCase().equals(myCard.toLowerCase())) {
            Toast.makeText(getApplicationContext(), "Correct guess!", Toast.LENGTH_LONG).show();
            correctGuess = true;
        }
        else {
            Toast.makeText(getApplicationContext(), "Incorrect guess :(", Toast.LENGTH_LONG).show();
        }

        updateFirebaseAndExit(correctGuess);

    }


    public void onSkipButton(android.view.View myView) {
        Toast.makeText(getApplicationContext(), "Skipping guess", Toast.LENGTH_LONG).show();
        updateFirebaseAndExit(false);
    }


    private void updateFirebaseAndExit(boolean correctGuess) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("games/game1");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(correctGuess) {
                    handleCorrectGuess(myRef);
                }


                long totalGuesses = (long)snapshot.child("guessesThisTurn").getValue();
                if ((totalGuesses+1)%3 == 0) {
                    myRef.child("guessesThisTurn").setValue(0);
                    myRef.child("gameStatus").setValue("hints");
                }
                else {
                    myRef.child("guessesThisTurn").setValue(totalGuesses + 1);
                }
                Context context = getApplicationContext();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        "preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("nextTurn", "hints");
                editor.apply();
                Log.d("myDebug", "my new pref is: " + sharedPref.getString("nextTurn", ""));
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void handleCorrectGuess(DatabaseReference myRef) {

        cardInd += 1;
        if (cardInd >= cards.size()) {
            Toast.makeText(getApplicationContext(), "You have now completed the game!", Toast.LENGTH_LONG);
        }
        else {
            myRef.child("players").child(emailToPlayer.get(myEmail)).child("currentCardInfo")
                    .child("cardName").setValue(cards.get(cardInd));
            //myRef.child("players").child(emailToPlayer.get(myEmail)).child("currentCardInfo")
            //        .child("hints").setValue(null);
        }

    }


    public void getData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("games/game1/players");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot player : snapshot.getChildren()) {
                    String email = (String) player.child("email").getValue();
                    if (email.equals(myEmail)) {
                        myCard = (String) player.child("currentCardInfo/cardName").getValue();
                        DataSnapshot cardSnap = player.child("cards");
                        int i = 0;
                        for (DataSnapshot card : cardSnap.getChildren()) {
                            String strCard = (String)card.getValue();
                            cards.add(strCard);
                            if (strCard.equals(myCard)) {
                                cardInd = i;
                            }
                            i += 1;
                        }
                        Log.d("myDebug", "my card is: " + myCard);
                        DataSnapshot hintSnapshot = player.child("currentCardInfo/hints");
                        for (DataSnapshot snap : hintSnapshot.getChildren()) {
                            String hint = (String) snap.child("hint").getValue();
                            String name = (String) snap.child("user").getValue();
                            HintInfo hInfo = new HintInfo(hint, name);
                            allHints.add(hInfo);
                            Log.d("myDebug", "hint is: " + hint + " and name is: " + name);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                }



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void listenForNextTurn() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("games/game1/gameStatus");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Context context = getApplicationContext();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        "preferences", Context.MODE_PRIVATE);
                String guessOrHint = sharedPref.getString("nextTurn", "guesses");
                String gameStatus = (String)dataSnapshot.getValue();
                if(guessOrHint.equals(gameStatus)) {
                    Log.d("myDebug", "can now perform " + gameStatus);
                }
                else {
                    Log.d("myDebug", "still need to wait for everyone to complete their turn");
                }
                Log.d("myDebug", "Game status is now: " + gameStatus);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("myDebug", "The read failed: " + databaseError.getCode());
            }
        });
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