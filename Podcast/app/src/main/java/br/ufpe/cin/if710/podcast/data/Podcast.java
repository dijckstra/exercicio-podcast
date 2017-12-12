package br.ufpe.cin.if710.podcast.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.database.Cursor;

import br.ufpe.cin.if710.podcast.data.source.local.PodcastPersistenceContract;

@Entity(tableName = "episodes")
public class Podcast {

    public static final int STATE_NOT_DOWNLOADED = 0;
    public static final int STATE_DOWNLOADED = 1;
    public static final int STATE_PLAYING = 2;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private long id;
    private String title;
    private String description;
    @ColumnInfo(name = "pub_date")
    private String pubDate;
    private String link;
    @ColumnInfo(name = "download_link")
    private String downloadLink;
    private int state;
    @ColumnInfo(name = "download_uri")
    private String fileUri;

    @Ignore
    public Podcast(String title, String description,
                      String pubDate, String link, String downloadLink) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
        this.downloadLink = downloadLink;
        this.state = STATE_NOT_DOWNLOADED;
        this.fileUri = null;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
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
