package com.example.firebaseAdvanced;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// Activity to show and append List of Tracks of an Artist

public class AddTrackActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textViewArtistName;
    private EditText editTextTrackName;
    private SeekBar seekBarRating;
    private ListView listViewTrack;
    private Button buttonAddTrack;

    DatabaseReference databaseTrack;

    List<Track> listOfTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        textViewArtistName = (TextView)findViewById(R.id.textViewArtistName);
        editTextTrackName = (EditText) findViewById(R.id.editTextTrackName);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarRating);
        listViewTrack = (ListView) findViewById(R.id.listViewTrack);
        buttonAddTrack = (Button) findViewById(R.id.buttonAddTrack);
        listOfTracks = new ArrayList<>();

        // Passing data from MainActivity to this activity
        Intent intent = getIntent();
        String id = intent.getStringExtra(MainActivity.ARTIST_ID);
        String name = intent.getStringExtra(MainActivity.ARTIST_NAME);

        textViewArtistName.setText(name);

        // Creating new node (would be new table in SQL) with specified artist id
        databaseTrack = FirebaseDatabase.getInstance().getReference("tracks").child(id);

        buttonAddTrack.setOnClickListener(this);
    }

    private void saveTrack(){
        String trackName = editTextTrackName.getText().toString().trim();
        int rating = seekBarRating.getProgress();

        if(!TextUtils.isEmpty(trackName)){
            // getting new Id every time button is pushed
            String id = databaseTrack.push().getKey();

            Track track = new Track(id, trackName, rating);

            databaseTrack.child(id).setValue(track);

            Toast.makeText(this, "Track Saved Successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Track Name Should Not Be Empty !!", Toast.LENGTH_LONG).show();
        }
    }

    // function to continuously update database
    @Override
    protected void onStart() {
        super.onStart();

        databaseTrack.addValueEventListener(new ValueEventListener() {
            @Override
            // This function will be executed every time we change anything inside in database
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listOfTracks.clear();

                // Loop through all the values of the database
                for(DataSnapshot trackSnapshot : dataSnapshot.getChildren()){
                    Track track = trackSnapshot.getValue(Track.class);
                    listOfTracks.add(track);
                }

                // showing the list on AddTrackActivity
                TrackList trackListAdapter = new TrackList(AddTrackActivity.this, listOfTracks);
                listViewTrack.setAdapter(trackListAdapter);
            }
            // Function will be run any time an error happens
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == buttonAddTrack)
        {
            saveTrack();
        }
    }
}
