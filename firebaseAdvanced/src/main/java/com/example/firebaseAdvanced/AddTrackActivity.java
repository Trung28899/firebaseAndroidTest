package com.example.firebaseAdvanced;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTrackActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textViewArtistName;
    private EditText editTextTrackName;
    private SeekBar seekBarRating;
    private ListView listViewTrack;
    private Button buttonAddTrack;

    DatabaseReference databaseTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        textViewArtistName = (TextView)findViewById(R.id.textViewArtistName);
        editTextTrackName = (EditText) findViewById(R.id.editTextTrackName);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarRating);
        listViewTrack = (ListView) findViewById(R.id.listViewTrack);
        buttonAddTrack = (Button) findViewById(R.id.buttonAddTrack);

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

    @Override
    public void onClick(View view) {
        if(view == buttonAddTrack)
        {
            saveTrack();
        }
    }
}
