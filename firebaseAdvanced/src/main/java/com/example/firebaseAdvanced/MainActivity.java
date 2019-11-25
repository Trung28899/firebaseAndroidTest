package com.example.firebaseAdvanced;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// Basic pusing data to database

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextName;
    private Button buttonAdd;
    private Spinner spinnerGenres;

    private DatabaseReference databaseArtist;

    ListView listViewArtists;

    List<Artist> artistList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtist = FirebaseDatabase.getInstance().getReference("artists");

        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonAdd = (Button) findViewById(R.id.buttonAddArtist);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);
        listViewArtists = (ListView) findViewById(R.id.listViewArtists);

        artistList = new ArrayList<>();

        buttonAdd.setOnClickListener(this);
    }

    private void addArtist(){
        String name = editTextName.getText().toString().trim();
        String genres = spinnerGenres.getSelectedItem().toString();
        String id;

        if(!TextUtils.isEmpty(name)){
            // This will generate a store a new unique id for database everytime the button is clicked
            id = databaseArtist.push().getKey();

            // Make a new artist object, see in Artist.java file
            Artist artist = new Artist(id, name, genres);

            // send the data to database
            databaseArtist.child(id).setValue(artist);

            Toast.makeText(this, "Artist Added", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this,"Enter a Name !!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseArtist.addValueEventListener(new ValueEventListener() {
            // This function will be executed every time we change anything inside in database
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Clear the artist before execute the code below
                artistList.clear();

                // Loop through all the values of the database
                for(DataSnapshot artistSnapshot : dataSnapshot.getChildren()){
                    Artist artist = artistSnapshot.getValue(Artist.class);

                    // adding artists into artist List
                    artistList.add(artist);
                }

                // See in Artist.java
                ArtistList adapter = new ArtistList(MainActivity.this, artistList);
                listViewArtists.setAdapter(adapter);



            }

            // This function will be executed every time we have any error
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == buttonAdd){
            addArtist();
        }
    }
}
