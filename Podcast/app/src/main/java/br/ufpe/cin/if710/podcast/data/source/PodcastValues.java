package br.ufpe.cin.if710.podcast.data.source;

import android.content.ContentValues;

import br.ufpe.cin.if710.podcast.data.Podcast;
import br.ufpe.cin.if710.podcast.data.source.local.PodcastPersistenceContract.PodcastEntry;

public class PodcastValues {
    public static ContentValues fromPodcast(Podcast podcast) {
        ContentValues values = new ContentValues();
        values.put(PodcastEntry.COLUMN_NAME_TITLE, podcast.getTitle());
        values.put(PodcastEntry.COLUMN_NAME_DESCRIPTION, podcast.getDescription());
        values.put(PodcastEntry.COLUMN_NAME_PUB_DATE, podcast.getPubDate());
        values.put(PodcastEntry.COLUMN_NAME_LINK, podcast.getLink());
        values.put(PodcastEntry.COLUMN_NAME_DOWNLOAD_LINK, podcast.getDownloadLink());
        values.put(PodcastEntry.COLUMN_NAME_STATE, podcast.getState());
        return values;
    }

    public static Podcast toPodcast(ContentValues values) {
        return new Podcast(values.getAsString(PodcastEntry.COLUMN_NAME_TITLE),
                values.getAsString(PodcastEntry.COLUMN_NAME_DESCRIPTION),
                values.getAsString(PodcastEntry.COLUMN_NAME_PUB_DATE),
                values.getAsString(PodcastEntry.COLUMN_NAME_LINK),
                values.getAsString(PodcastEntry.COLUMN_NAME_DOWNLOAD_LINK));
    }
}
