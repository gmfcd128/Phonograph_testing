package com.kabouzeid.gramophone;

import android.content.Context;

import androidx.test.annotation.UiThreadTest;
import androidx.test.core.app.ApplicationProvider;

import com.kabouzeid.gramophone.loader.PlaylistLoader;
import com.kabouzeid.gramophone.loader.PlaylistSongLoader;
import com.kabouzeid.gramophone.loader.SongLoader;
import com.kabouzeid.gramophone.model.Playlist;
import com.kabouzeid.gramophone.model.PlaylistSong;
import com.kabouzeid.gramophone.model.Song;
import com.kabouzeid.gramophone.util.PlaylistsUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PlaylistUtilIntegrationTest {
    Context context;

    @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();
        List<Playlist> allPlaylists = PlaylistLoader.getAllPlaylists(context);
        PlaylistsUtil.deletePlaylists(context, allPlaylists);
    }

    @Test
    @UiThreadTest
    public void testCreatePlaylist() {
        long playlistId = PlaylistsUtil.createPlaylist(context, "test");
        Assert.assertEquals("test", PlaylistsUtil.getNameForPlaylist(context, playlistId));
        // test repeated call with playlist name already exists
        long playlistIdAgain = PlaylistsUtil.createPlaylist(context, "test");
        Assert.assertEquals(playlistId, playlistIdAgain);
        // verify playlist using loader
        Playlist playlist = PlaylistLoader.getPlaylist(context, playlistId);
        Assert.assertEquals(playlistId, playlist.id);
        Assert.assertEquals("test", playlist.name);
        playlist = PlaylistLoader.getPlaylist(context, "test");
        Assert.assertEquals(playlistId, playlist.id);
        Assert.assertEquals("test", playlist.name);
    }

    @Test
    @UiThreadTest
    public void testCreatePlaylistNameNull() {
        // test for playlist name null
        Assert.assertEquals(-1, PlaylistsUtil.createPlaylist(context, null));
    }

    @Test
    @UiThreadTest
    public void testDeletePlaylist() {
        long playlistId = PlaylistsUtil.createPlaylist(context, "test");
        Playlist test = PlaylistLoader.getPlaylist(context, playlistId);
        playlistId = PlaylistsUtil.createPlaylist(context, "test2");
        Playlist test2 = PlaylistLoader.getPlaylist(context, playlistId);
        playlistId = PlaylistsUtil.createPlaylist(context, "test3");
        Playlist test3 = PlaylistLoader.getPlaylist(context, playlistId);
        playlistId = PlaylistsUtil.createPlaylist(context, "test4");
        Playlist test4 = PlaylistLoader.getPlaylist(context, playlistId);
        playlistId = PlaylistsUtil.createPlaylist(context, "test5");
        Playlist test5 = PlaylistLoader.getPlaylist(context, playlistId);
        // delete 1 playlist
        List<Playlist> playlistsToDelete = new ArrayList<>();
        playlistsToDelete.add(test);
        PlaylistsUtil.deletePlaylists(context, playlistsToDelete);
        List<Playlist> existingPlaylists = PlaylistLoader.getAllPlaylists(context);
        Assert.assertEquals(4, existingPlaylists.size());
        // test delete another two in a row
        playlistsToDelete.clear();
        playlistsToDelete.add(test3);
        playlistsToDelete.add(test4);
        PlaylistsUtil.deletePlaylists(context, playlistsToDelete);
        existingPlaylists = PlaylistLoader.getAllPlaylists(context);
        Assert.assertEquals(2, existingPlaylists.size());
        // verify remaining playlists
        Assert.assertEquals(false, PlaylistsUtil.doesPlaylistExist(context,"test"));
        Assert.assertEquals(true, PlaylistsUtil.doesPlaylistExist(context,"test2"));
        Assert.assertEquals(false, PlaylistsUtil.doesPlaylistExist(context,"test3"));
        Assert.assertEquals(false, PlaylistsUtil.doesPlaylistExist(context,"test4"));
        Assert.assertEquals(true, PlaylistsUtil.doesPlaylistExist(context,"test5"));
    }

    @Test
    @UiThreadTest
    public void testPlaylistRename() {
        long playlistId = PlaylistsUtil.createPlaylist(context, "test");
        PlaylistsUtil.renamePlaylist(context, playlistId, "new playlist");
        List<Playlist> existingPlaylists = PlaylistLoader.getAllPlaylists(context);
        Assert.assertEquals(1, existingPlaylists.size());
        Assert.assertEquals("new playlist", existingPlaylists.get(0).name);
    }

    @Test
    @UiThreadTest
    public void testAddSongToPlaylist() {
        long playlistId = PlaylistsUtil.createPlaylist(context, "test");
        List<Song> songs = SongLoader.getAllSongs(context);
        Song test1 = songs.get(0);
        Song test2 = songs.get(1);
        PlaylistsUtil.addToPlaylist(context, test1, playlistId, false);
        PlaylistsUtil.addToPlaylist(context, test2, playlistId, false);
        List<PlaylistSong> testPlaylist = PlaylistSongLoader.getPlaylistSongList(context, playlistId);
        Assert.assertEquals(2, testPlaylist.size());
        Assert.assertEquals(test1.id, testPlaylist.get(0).id);
        Assert.assertEquals(test2.id, testPlaylist.get(1).id);
        Assert.assertEquals(test1.title, testPlaylist.get(0).title);
        Assert.assertEquals(test2.title, testPlaylist.get(1).title);
    }

    @Test
    @UiThreadTest
    public void testPlaylistSongReorder() {
        long playlistId = PlaylistsUtil.createPlaylist(context, "test");
        List<Song> songs = SongLoader.getAllSongs(context);
        Song test1 = songs.get(0);
        Song test2 = songs.get(1);
        Song test3 = songs.get(2);
        PlaylistsUtil.addToPlaylist(context, test1, playlistId, false);
        PlaylistsUtil.addToPlaylist(context, test2, playlistId, false);
        PlaylistsUtil.addToPlaylist(context, test3, playlistId, false);
        PlaylistsUtil.moveItem(context, playlistId, 0, 2);
        List<PlaylistSong> testPlaylist = PlaylistSongLoader.getPlaylistSongList(context, playlistId);
        Assert.assertEquals(test2.id, testPlaylist.get(0).id);
        Assert.assertEquals(test3.id, testPlaylist.get(1).id);
        Assert.assertEquals(test1.id, testPlaylist.get(2).id);
        Assert.assertEquals(test2.title, testPlaylist.get(0).title);
        Assert.assertEquals(test3.title, testPlaylist.get(1).title);
        Assert.assertEquals(test1.title, testPlaylist.get(2).title);
    }
}
