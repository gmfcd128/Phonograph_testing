package com.kabouzeid.gramophone.mockActivities;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.kabouzeid.gramophone.ui.activities.tageditor.SongTagEditorActivity;

import java.util.ArrayList;
import java.util.List;

public class MockSongTagEditorActivity extends SongTagEditorActivity {
    private List<String> songPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        songPaths = new ArrayList<>();
        Bundle intentExtras = getIntent().getExtras();
        if (intentExtras != null) {
            songPaths.add(intentExtras.getString("test_song_path"));
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected List<String> getSongPaths() {
        return songPaths;
    }

    @Override
    public String getSongTitle() {
        return super.getSongTitle();
    }

    @Override
    public String getAlbumTitle() {
        return super.getAlbumTitle();
    }

    @Override
    public String getArtistName() {
        return super.getArtistName();
    }

    @Override
    public String getTrackNumber() {
        return super.getTrackNumber();
    }

    @Override
    public String getGenreName() {
        return super.getGenreName();
    }

    @Override
    public String getSongYear() {
        return super.getSongYear();
    }

    @Override
    public String getLyrics() {
        return super.getLyrics();
    }

    @Override
    public void save() {
        super.save();
    }
}
