package com.example.asyncgame;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class Hints extends AppCompatActivity {

    private static ArrayList<GuessPage.HintInfo> allHints = new ArrayList<>();
    ;
    private static int playerNo;
    RecyclerView hintDisplay;
    GuessAdapter myAdapter;
    String userName;

    public Hints() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hints);

        getUserName();
        //Log.d("myDebug", "username is "+ userName);
        hintDisplay = findViewById(R.id.guessList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        hintDisplay.setLayoutManager(layoutManager);
        myAdapter = new GuessAdapter(allHints);
        hintDisplay.setAdapter(myAdapter);
        //display the guesses in the fragment
        //MainActivity main = (MainActivity)getActivity();

        //set button handler for checkbox
        CheckBox b = findViewById(R.id.hintCheckBox);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushHint();
                b.setChecked(false);
            }
        });

        //playerNo = 1;
        //updateView();

    }

    public void getUserName(){
        FirebaseAuth fAuth;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        String userID;
        fAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("users").child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                User user = task.getResult().getValue(User.class);
                if (user != null) {
                    userName=user.getUserName();
                    playerNo = 1;
                    updateView();
                }
            }
        });
    }



    public void pushHint() {
        EditText et= findViewById(R.id.hintText);
        GuessPage.HintInfo hInfo = new GuessPage.HintInfo(et.getText().toString(), userName);
        String playerDisplayed = "player" + Integer.toString(playerNo);
        String pathToUpdate="games/game1/players/"+playerDisplayed+"/currentCardInfo/hints";
        DatabaseReference emaRef = FirebaseDatabase.getInstance().getReference(pathToUpdate);
        emaRef.push().setValue(hInfo);

        playerNo+=1;
        playerDisplayed = "player" + Integer.toString(playerNo);
        updateView();

    }

    public void updateView() {
        getData();
    }


    public void getData() {
        //remove previous hints before displaying new hints
        allHints.clear();
        DatabaseReference emaRef = FirebaseDatabase.getInstance().getReference("games/game1/players");
        String playerDisplayed = "player" + Integer.toString(playerNo);
        emaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child(playerDisplayed).exists()){
                    //switch intents to games page
                    finish();
                    return;
                }
                if (snapshot.child(playerDisplayed).child("username").getValue().toString().equals(userName)) {
                    playerNo += 1;
                }
                if(!snapshot.child(playerDisplayed).exists()){
                    //switch intents to games page
                    finish();
                    return;
                }
                String playerDisplayed = "player" + Integer.toString(playerNo);
                //Set views for the objects
                String nameText = snapshot.child(playerDisplayed).child("username").getValue().toString();
                String objText = snapshot.child(playerDisplayed).child("currentCardInfo/cardName").getValue().toString();
                setTitle(objText, nameText);
                for (DataSnapshot snap : snapshot.child(playerDisplayed).child("currentCardInfo").child("hints").getChildren()) {
                    String hint = (String) snap.child("hint").getValue();
                    String name = (String) snap.child("user").getValue();
                    GuessPage.HintInfo hInfo = new GuessPage.HintInfo(hint, name);
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

    public void setTitle(String obj, String name) {
        TextView tv = findViewById(R.id.objectText);
        tv.setText(obj);
        tv = findViewById(R.id.nameText);
        tv.setText(name);
    }
}
