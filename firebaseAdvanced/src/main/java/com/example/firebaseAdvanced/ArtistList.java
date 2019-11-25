package com.example.firebaseAdvanced;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

// JAVA class for showing the List of Artist in the ListView of MainActivity

public class ArtistList extends ArrayAdapter<Artist> {

    private Activity context;
    private List<Artist> listOfArtists;

    public ArtistList(Activity context, List<Artist> listOfArtists){
        super(context, R.layout.list_layout, listOfArtists);
        this.context = context;
        this.listOfArtists = listOfArtists;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenres = (TextView) listViewItem.findViewById(R.id.textViewGenres);

        Artist artist = listOfArtists.get(position);

        textViewName.setText(artist.getArtistName());
        textViewGenres.setText(artist.getArtistGenre());

        return listViewItem;
    }
}
