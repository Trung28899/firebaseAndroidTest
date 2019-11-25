package com.example.firebaseAdvanced;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// Adding Artist and Showing List of Artists, retrieved from the database

public class MainActivity extends AppCompatActivity {
    public static final String ARTIST_NAME = "artistname";
    public static final String ARTIST_ID = "artistId";

    private EditText editTextName;
    private Button buttonAdd;
    private Spinner spinnerGenres;

    private DatabaseReference databaseArtist;

    ListView listViewArtists;
    List<Artist> listOfArtists;


    // Function for declarations, Buttons and ListView Items listeners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtist = FirebaseDatabase.getInstance().getReference("artists");

        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonAdd = (Button) findViewById(R.id.buttonAddArtist);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);
        listViewArtists = (ListView) findViewById(R.id.listViewArtists);

        listOfArtists = new ArrayList<>();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 addArtist();
             }
        });

                // Item Click Listener for Items in ListView
                listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Artist artist = listOfArtists.get(position);
                        Intent intent = new Intent(getApplicationContext(), AddTrackActivity.class);

                        intent.putExtra(ARTIST_ID, artist.getArtistId());
                        intent.putExtra(ARTIST_NAME, artist.getArtistName());

                        startActivity(intent);
                    }
                });

        // Item in ListView listener for Long Click (Click and Hold)
        listViewArtists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = listOfArtists.get(position);

                showUpdateDialog(artist.getArtistId(), artist.getArtistName());

                return false;
            }
        });
    }

    // Function to Display functionally the Diaglog for updating fields
    private void showUpdateDialog(final String artistId, String artistName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) findViewById(R.id.update);
        final Spinner spinnerGenres1 = (Spinner) findViewById(R.id.spinner);

        dialogBuilder.setTitle("Updating Artist: " + artistName);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 String name = editTextName.getText().toString().trim();
                String genre = spinnerGenres1.getSelectedItem().toString();

                if(TextUtils.isEmpty(name)){
                    editTextName.setError("Name Required");
                    return;
                }
                updateArtist(artistId, name, genre);
                // close dialog after update
                alertDialog.dismiss();
                */
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Function for Adding Artist
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

    // Function for updating data from database
    @Override
    protected void onStart() {
        super.onStart();

        databaseArtist.addValueEventListener(new ValueEventListener() {
            // This function will be executed every time we change anything inside in database
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Clear the artist before execute the code below
                listOfArtists.clear();

                // Loop through all the values of the database
                for(DataSnapshot artistSnapshot : dataSnapshot.getChildren()){
                    Artist artist = artistSnapshot.getValue(Artist.class);

                    // adding artists into artist List
                    listOfArtists.add(artist);
                }

                // See in Artist.java
                ArtistList adapter = new ArtistList(MainActivity.this, listOfArtists);
                listViewArtists.setAdapter(adapter);
            }

            // This function will be executed every time we have any error
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Function for updating Artist information in the
    private boolean updateArtist(String id, String name, String genre){
        // Getting the particular artist that need to be updated
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("artists").child(id);
        // create a new artist with new id, name and genre
        Artist artist = new Artist(id, name, genre);

        // Over-writting the new artist into a specified id
        databaseReference.setValue(artist);

        Toast.makeText(this, "Artist is Updated Successfully", Toast.LENGTH_LONG).show();

        return true;
    }

}
