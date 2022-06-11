package com.kabouzeid.gramophone;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

import com.kabouzeid.gramophone.helper.MusicPlayerRemote;
import com.kabouzeid.gramophone.loader.SongLoader;
import com.kabouzeid.gramophone.model.Song;
import com.kabouzeid.gramophone.ui.activities.MainActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

public class MusicPlayerRemoteTest {
    Context context;
    private MusicPlayerRemote.ServiceToken serviceToken;
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        context = mActivityRule.getActivity();
        serviceToken = MusicPlayerRemote.bindToService(context, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                System.out.println("MusicService connected");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                System.out.println("MusicService disconnected");
            }
        });
        MusicPlayerRemote.clearQueue();
    }

    @Test
    @UiThreadTest
    public void testAddToNext() throws Exception{
        List<Song> songs = SongLoader.getAllSongs(context);
        MusicPlayerRemote.enqueue(songs.get(0));
        MusicPlayerRemote.enqueue(songs.get(1));
        MusicPlayerRemote.enqueue(songs.get(2));
        //add 4th track to next play
        MusicPlayerRemote.playNext(songs.get(3));
        MusicPlayerRemote.playSongAt(0);
        Thread.sleep(1000);
        MusicPlayerRemote.playNextSong();
        Thread.sleep(1000);
        Assert.assertEquals(songs.get(3), MusicPlayerRemote.getCurrentSong());

    }
}
