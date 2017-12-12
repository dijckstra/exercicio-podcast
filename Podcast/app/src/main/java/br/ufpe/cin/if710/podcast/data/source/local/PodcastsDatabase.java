package br.ufpe.cin.if710.podcast.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.ufpe.cin.if710.podcast.data.Podcast;

@Database(entities = {Podcast.class}, version = 1, exportSchema = false)
public abstract class PodcastsDatabase extends RoomDatabase {

    private static PodcastsDatabase INSTANCE;

    public abstract PodcastDao podcastDao();

    private static final Object lock = new Object();

    public static PodcastsDatabase getInstance(Context context) {
        synchronized (lock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        PodcastsDatabase.class, "Podcast.db")
                        .allowMainThreadQueries()
                        .build();
            }

            return INSTANCE;
        }
    }
}
