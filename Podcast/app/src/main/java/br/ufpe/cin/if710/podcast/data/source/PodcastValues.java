package br.ufpe.cin.if710.podcast.data.source;

import android.content.ContentValues;

import br.ufpe.cin.if710.podcast.data.Podcast;
import br.ufpe.cin.if710.podcast.data.source.local.PodcastPersistenceContract;

public class PodcastValues {
    public static ContentValues from(Podcast podcast) {
        ContentValues values = new ContentValues();
        values.put(PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_TITLE, podcast.getTitle());
        values.put(PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_DESCRIPTION, podcast.getDescription());
        values.put(PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_PUB_DATE, podcast.getPubDate());
        values.put(PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_LINK, podcast.getLink());
        values.put(PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_DOWNLOAD_LINK, podcast.getDownloadLink());
        return values;
    }
}
