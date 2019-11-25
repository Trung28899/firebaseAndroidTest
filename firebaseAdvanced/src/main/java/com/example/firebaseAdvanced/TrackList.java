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

// JAVA class for showing the List of Tracks in the ListView of AddTrackActivity

public class TrackList extends ArrayAdapter<Track> {

    private Activity context;
    private List<Track> listOfTracks;

    public TrackList(Activity context, List<Track> listOfTracks){
        super(context, R.layout.list_layout, listOfTracks);
        this.context = context;
        this.listOfTracks = listOfTracks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.tracklist_layout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewRating = (TextView) listViewItem.findViewById(R.id.textViewRating);

        Track track = listOfTracks.get(position);

        textViewName.setText(track.getTrackName());
        String ratingText = "Rating: " + String.valueOf(track.getTrackRating());
        textViewRating.setText(ratingText);

        return listViewItem;
    }

}
