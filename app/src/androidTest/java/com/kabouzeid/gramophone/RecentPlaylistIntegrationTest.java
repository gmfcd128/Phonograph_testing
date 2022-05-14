package com.kabouzeid.gramophone;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.kabouzeid.gramophone.loader.SongLoader;
import com.kabouzeid.gramophone.loader.TopAndRecentlyPlayedTracksLoader;
import com.kabouzeid.gramophone.model.Song;
import com.kabouzeid.gramophone.provider.HistoryStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.List;

public class RecentPlaylistIntegrationTest {
    Context context;
    List<Song> songs;

    @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();
        songs = SongLoader.getAllSongs(context);
        HistoryStore.getInstance(context).clear();
    }

    @Test
    public void testAddSingleWhenEmpty() {
        Song single = songs.get(0);
        HistoryStore.getInstance(context).addSongId(single.id);
        List<Song> recentlyPlayed = TopAndRecentlyPlayedTracksLoader.getRecentlyPlayedTracks(context);
        Assert.assertEquals(1, recentlyPlayed.size());
        Assert.assertEquals(single, recentlyPlayed.get(0));
    }

    @Test
    public void testAddMultipleSongWhenEmpty() {
        int songsToAdd = 10;
        List<Song> test = songs.subList(0, songsToAdd);
        for (Song song : test) {
            HistoryStore.getInstance(context).addSongId(song.id);
        }
        List<Song> recentlyPlayed = TopAndRecentlyPlayedTracksLoader.getRecentlyPlayedTracks(context);
        Assert.assertEquals(songsToAdd, recentlyPlayed.size());
        for (int i = 0; i < songsToAdd; i++) {
            Assert.assertEquals(songs.get(i), recentlyPlayed.get(songsToAdd - i - 1));
        }
    }

    @Test
    public void testAddExistingSong() {
        int songsToAdd = 10;
        List<Song> test = songs.subList(0, songsToAdd);
        for (Song song : test) {
            HistoryStore.getInstance(context).addSongId(song.id);
        }
        HistoryStore.getInstance(context).addSongId(test.get(8).id);
        HistoryStore.getInstance(context).addSongId(test.get(5).id);
        HistoryStore.getInstance(context).addSongId(test.get(7).id);
        List<Song> recentlyPlayed = TopAndRecentlyPlayedTracksLoader.getRecentlyPlayedTracks(context);
        Assert.assertEquals(songsToAdd, recentlyPlayed.size());
        Assert.assertEquals(test.get(7), recentlyPlayed.get(0));
        Assert.assertEquals(test.get(5), recentlyPlayed.get(1));
        Assert.assertEquals(test.get(8), recentlyPlayed.get(2));
        Assert.assertEquals(test.get(9), recentlyPlayed.get(3));
        Assert.assertEquals(test.get(6), recentlyPlayed.get(4));
        Assert.assertEquals(test.get(4), recentlyPlayed.get(5));
        Assert.assertEquals(test.get(3), recentlyPlayed.get(6));
        Assert.assertEquals(test.get(2), recentlyPlayed.get(7));
        Assert.assertEquals(test.get(1), recentlyPlayed.get(8));
        Assert.assertEquals(test.get(0), recentlyPlayed.get(9));
    }

    @Test
    public void testOldRecordTruncate() {
        int songsToAdd = 101;
        List<Song> test = songs.subList(0, songsToAdd);
        for (Song song : test) {
            HistoryStore.getInstance(context).addSongId(song.id);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        List<Song> recentlyPlayed = TopAndRecentlyPlayedTracksLoader.getRecentlyPlayedTracks(context);
        Assert.assertEquals(100, recentlyPlayed.size());
        for (int i = 0; i < 100; i++) {
            Assert.assertEquals(songs.get(i + 1), recentlyPlayed.get(100 - i - 1));
        }
    }

    @Test
    public void insertHistoryWithInvalidSongId() {
        HistoryStore.getInstance(context).addSongId(-1);;
        List<Song> recentlyPlayed = TopAndRecentlyPlayedTracksLoader.getRecentlyPlayedTracks(context);
        Assert.assertEquals(0, recentlyPlayed.size());
    }



}
