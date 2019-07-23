package com.example.nature_hunt;

import android.content.Context;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.example.nature_hunt.db.local.LocalDatabase;
import com.example.nature_hunt.db.local.LocalDatabaseAccessor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Tests our ability to write to the local database.
 */
@RunWith(AndroidJUnit4.class)
public class LocalDbTest {
    private LocalDatabase db;
    private LocalDatabaseAccessor dao;

    @Before
    public void createDatabase() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(appContext, LocalDatabase.class).build();
        dao = db.getAccessor();
    }

    @After
    public void cleanUp() {
        dao.clearHuntProgress(1);
        dao.clearHuntProgress(2);
        db.close();
    }

    @Test
    public void huntProgressTest() {
        // Make sure we're starting from scratch
        assertEquals(dao.getSpeciesFoundFromHunt(1).size(), 0);
        assertEquals(dao.getSpeciesFoundFromHunt(2).size(), 0);

        // Make some progress on one hunt
        dao.markSpeciesAsFound(1, 106);
        dao.markSpeciesAsFound(1, 794);
        dao.markSpeciesAsFound(1, 515);
        assertEquals(dao.getSpeciesFoundFromHunt(1).size(), 3);

        // Make some progress on several hunts
        dao.markSpeciesAsFound(1, 267);
        dao.markSpeciesAsFound(1, 889);
        dao.markSpeciesAsFound(2, 889);
        dao.markSpeciesAsFound(2, 106);
        dao.markSpeciesAsFound(2, 448);
        dao.markSpeciesAsFound(2, 259);
        assertEquals(dao.getSpeciesFoundFromHunt(1).size(), 5);
        assertEquals(dao.getSpeciesFoundFromHunt(2).size(), 4);

        // Clear progress on one hunt, make sure the other hunt is undisturbed
        dao.clearHuntProgress(1);
        assertEquals(dao.getSpeciesFoundFromHunt(1).size(), 0);
        assertEquals(dao.getSpeciesFoundFromHunt(2).size(), 4);

        // Find the same species twice, it should only count once
        dao.markSpeciesAsFound(1, 360);
        dao.markSpeciesAsFound(1, 360);
        assertEquals(dao.getSpeciesFoundFromHunt(1).size(), 1);
    }
}
