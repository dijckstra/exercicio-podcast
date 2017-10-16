package br.ufpe.cin.if710.podcast.data;

import android.database.Cursor;

import br.ufpe.cin.if710.podcast.data.source.local.PodcastPersistenceContract;

public class Podcast {

    public static final int STATE_NOT_DOWNLOADED = 0;
    public static final int STATE_DOWNLOADED = 1;
    public static final int STATE_PLAYING = 2;

    private long id;
    private final String title;
    private final String description;
    private final String pubDate;
    private final String link;
    private final String downloadLink;
    private final int state;
    private String fileUri;

    public Podcast(String title, String description,
                   String pubDate, String link, String downloadLink) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
        this.downloadLink = downloadLink;
        this.state = STATE_NOT_DOWNLOADED;
    }

    public Podcast(long id, String title, String description,
                   String pubDate, String link, String downloadLink,
                   int state, String fileUri) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
        this.downloadLink = downloadLink;
        this.state = state;
        this.fileUri = fileUri;
    }


    public long getId() {
        return id;
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

    public int getState() {
        return state;
    }

    public String getFileUri() {
        return fileUri;
    }

    public String getFileName() {
        return id + ".mp3";
    }

    public static Podcast from(Cursor cursor) {
        long id = cursor.getInt(cursor.getColumnIndexOrThrow(
                PodcastPersistenceContract.PodcastEntry._ID));
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
        String fileUri = cursor.getString(cursor.getColumnIndexOrThrow(
                PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_FILE_URI));
        int state = cursor.getInt(cursor.getColumnIndexOrThrow(
                PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_STATE));

        return new Podcast(id, title, description, pubDate, link, downloadLink, state, fileUri);
    }
}