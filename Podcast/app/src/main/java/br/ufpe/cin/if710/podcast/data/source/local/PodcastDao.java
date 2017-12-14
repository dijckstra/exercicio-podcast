package br.ufpe.cin.if710.podcast.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

import br.ufpe.cin.if710.podcast.data.Podcast;

@Dao
public interface PodcastDao {

    @Query("SELECT * FROM episodes")
    Cursor getPodcasts();

    @Query("SELECT * FROM episodes WHERE _id = :id")
    Cursor getPodcast(long id);

    @Query("SELECT * FROM episodes WHERE pub_date = :pubDate")
    Cursor getPodcast(String pubDate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long savePodcast(Podcast podcast);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] savePodcasts(List<Podcast> podcasts);

    @Query("UPDATE episodes SET download_uri = :uri WHERE _id = :podcastId")
    int setFileUri(long podcastId, String uri);

    @Query("UPDATE episodes SET state = :state WHERE _id = :podcastId")
    int setPodcastState(long podcastId, int state);
}
