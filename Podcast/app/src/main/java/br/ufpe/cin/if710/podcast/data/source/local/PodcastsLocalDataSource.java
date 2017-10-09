package br.ufpe.cin.if710.podcast.data.source.local;

import android.content.ContentResolver;
import android.content.ContentValues;

import br.ufpe.cin.if710.podcast.data.Podcast;
import br.ufpe.cin.if710.podcast.data.source.PodcastValues;
import br.ufpe.cin.if710.podcast.data.source.PodcastsDataSource;

public class PodcastsLocalDataSource implements PodcastsDataSource {

    private static PodcastsLocalDataSource INSTANCE;
    private final ContentResolver contentResolver;

    private PodcastsLocalDataSource(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public static PodcastsLocalDataSource getInstance(ContentResolver contentResolver) {
        if (INSTANCE == null) {
            INSTANCE = new PodcastsLocalDataSource(contentResolver);
        }
        return INSTANCE;
    }

    @Override
    public void getPodcasts(GetPodcastsCallback callback) {
        // since the CursorLoader is loading the data this is not implemented
    }

    public void savePodcast(Podcast podcast) {
        ContentValues values = PodcastValues.from(podcast);
        contentResolver.insert(PodcastPersistenceContract.PodcastEntry.buildPodcastsUri(), values);
    }

    @Override
    public void deleteAllPodcasts() {
        contentResolver.delete(PodcastPersistenceContract.PodcastEntry.buildPodcastsUri(), null, null);
    }
}
