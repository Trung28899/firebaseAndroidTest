package com.example.firebaseAdvanced;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextName;
    private Button buttonAdd;
    private Spinner spinnerGenres;

    private DatabaseReference databaseArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtist = FirebaseDatabase.getInstance().getReference("artists");

        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonAdd = (Button) findViewById(R.id.buttonAddArtist);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);

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
    public void onClick(View view) {
        if(view == buttonAdd){
            addArtist();
        }
    }
}
