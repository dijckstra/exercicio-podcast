package br.ufpe.cin.if710.podcast.data.source;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import br.ufpe.cin.if710.podcast.data.source.local.PodcastPersistenceContract;

public class LoaderProvider {

    private final Context context;

    public LoaderProvider(Context context) {
        this.context = context;
    }

    public Loader<Cursor> createPodcastsLoader() {
        return new CursorLoader(
                context,
                PodcastPersistenceContract.PodcastEntry.buildPodcastsUri(),
                PodcastPersistenceContract.PodcastEntry.PODCAST_COLUMNS,
                null,
                null,
                null
        );
    }

    public Loader<Cursor> createPodcastLoader(int podcastId) {
        return new CursorLoader(
                context,
                PodcastPersistenceContract.PodcastEntry.buildPodcastsUriWith(podcastId),
                null,
                null,
                new String[] { String.valueOf(podcastId) },
                null
        );
    }
}
