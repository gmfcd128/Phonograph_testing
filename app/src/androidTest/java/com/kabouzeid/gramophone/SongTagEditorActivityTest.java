package com.kabouzeid.gramophone;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import com.kabouzeid.gramophone.mockActivities.MockSongTagEditorActivity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;



public class SongTagEditorActivityTest {
    List<String> songPaths;
    ActivityScenario<MockSongTagEditorActivity> activityScenario;


    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();


    @Before
    public void setUp() throws Exception {
        temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
        InputStream resourceInputStream = getClass().getClassLoader().getResourceAsStream("test.flac");
        File testFileCopy = temporaryFolder.newFile("test.flac");
        Files.copy(resourceInputStream, testFileCopy.toPath(), StandardCopyOption.REPLACE_EXISTING);
        songPaths = new ArrayList<>();
        songPaths.add(testFileCopy.getAbsolutePath());
    }

    @After
    public void tearDown() throws Exception {
        activityScenario.close();
    }


    @Test
    public void testSave() {
        Intent tagEditorIntent = new Intent(ApplicationProvider.getApplicationContext(), MockSongTagEditorActivity.class);
        tagEditorIntent.putExtra("test_song_path", songPaths.get(0));
        activityScenario = ActivityScenario.launch(tagEditorIntent);
        Espresso.onView(withId(R.id.title1)).perform(replaceText("test song title"));
        Espresso.onView(withId(R.id.title2)).perform(replaceText("test album title"));
        Espresso.onView(withId(R.id.artist)).perform(replaceText("test artist"));
        Espresso.onView(withId(R.id.genre)).perform(replaceText("pop"));
        Espresso.onView(withId(R.id.year)).perform(replaceText("2022"));
        Espresso.onView(withId(R.id.image_text)).perform(replaceText("01"));
        Espresso.onView(withId(R.id.lyrics)).perform(replaceText("Android Espresso UI Test"));
        activityScenario.onActivity((ActivityScenario.ActivityAction<MockSongTagEditorActivity>) activity -> {
            activity.save();
        });
        activityScenario.close();
        activityScenario = ActivityScenario.launch(tagEditorIntent);
        activityScenario.onActivity((ActivityScenario.ActivityAction<MockSongTagEditorActivity>) activity -> {
            Assert.assertEquals("test song title", activity.getSongTitle());
            Assert.assertEquals("test album title", activity.getAlbumTitle());
            Assert.assertEquals("test artist", activity.getArtistName());
            Assert.assertEquals("pop", activity.getGenreName());
            Assert.assertEquals("2022", activity.getSongYear());
            Assert.assertEquals("01", activity.getTrackNumber());
            Assert.assertEquals("Android Espresso UI Test", activity.getLyrics());
        });

    }


}
