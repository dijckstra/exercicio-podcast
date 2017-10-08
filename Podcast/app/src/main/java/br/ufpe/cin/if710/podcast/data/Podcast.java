package br.ufpe.cin.if710.podcast.data;

import android.database.Cursor;
import android.net.Uri;

import br.ufpe.cin.if710.podcast.data.source.local.PodcastPersistenceContract;

public class Podcast {

    private final String title;
    private final String description;
    private final String pubDate;
    private final String link;
    private final String downloadLink;
    private Uri fileUri;

    public Podcast(String title, String description, String pubDate, String link, String downloadLink) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
        this.downloadLink = downloadLink;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getLink() {
        return link;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    @Override
    public String toString() {
        return title;
    }

    public static Podcast from(Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndexOrThrow(
                PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(
                PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_DESCRIPTION));
        String pubDate = cursor.getString(cursor.getColumnIndexOrThrow(
                PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_PUB_DATE));
        String link = cursor.getString(cursor.getColumnIndexOrThrow(
                PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_LINK));
        String downloadLink = cursor.getString(cursor.getColumnIndexOrThrow(
                PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_DOWNLOAD_LINK));
        return new Podcast(title, description, pubDate, link, downloadLink);
    }
}