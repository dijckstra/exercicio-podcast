package br.ufpe.cin.if710.podcast.data.source;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;

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
}
