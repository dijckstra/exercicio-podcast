package br.ufpe.cin.if710.podcast.data.source;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.data.Podcast;
import br.ufpe.cin.if710.podcast.data.source.local.PodcastDao;
import br.ufpe.cin.if710.podcast.data.source.local.PodcastPersistenceContract;
import br.ufpe.cin.if710.podcast.data.source.local.PodcastsDatabase;

import static br.ufpe.cin.if710.podcast.data.source.local.PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_FILE_URI;
import static br.ufpe.cin.if710.podcast.data.source.local.PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_PUB_DATE;
import static br.ufpe.cin.if710.podcast.data.source.local.PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_STATE;

public class PodcastProvider extends ContentProvider {

    private static final String TAG = "PodcastProvider";

    private static final int PODCAST = 100;
    private static final int PODCAST_ITEM = 101;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private PodcastDao podcastDao;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PodcastPersistenceContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, PodcastPersistenceContract.PodcastEntry.TABLE_NAME, PODCAST);
        matcher.addURI(authority, PodcastPersistenceContract.PodcastEntry.TABLE_NAME + "/*", PODCAST_ITEM);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        podcastDao = PodcastsDatabase.getInstance(getContext()).podcastDao();
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PODCAST:
                return PodcastPersistenceContract.CONTENT_PODCAST_TYPE;
            case PODCAST_ITEM:
                return PodcastPersistenceContract.CONTENT_PODCAST_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (uriMatcher.match(uri)) {
            case PODCAST:
                retCursor = podcastDao.getPodcasts();
                break;
            case PODCAST_ITEM:
                retCursor = podcastDao.getPodcast(ContentUris.parseId(uri));
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri returnUri = null;

        switch (uriMatcher.match(uri)) {
            case PODCAST:
                // Check if the item to be inserted exists.
                // Since the podcast information from the server doesn't
                // have a unique identifier, the selection is done
                // by passing the publication date
                Cursor exists = podcastDao.getPodcast(values.getAsString(COLUMN_NAME_PUB_DATE));

                // If it doesn't exist, insert it.
                if (!exists.moveToLast()) {
                    long _id = podcastDao.savePodcast(PodcastValues.toPodcast(values));

                    if (_id > 0) {
                        returnUri = PodcastPersistenceContract.PodcastEntry.buildPodcastsUriWith(_id);
                    } else {
                        throw new SQLException("Failed to insert row into " + uri);
                    }
                }

                exists.close();
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] valuesArray) {
        int returnInt;

        switch (uriMatcher.match(uri)) {
            case PODCAST:
                List<Podcast> podcasts = new ArrayList<>();

                for (ContentValues values :
                        valuesArray) {
                    podcasts.add(PodcastValues.toPodcast(values));
                }

                long[] ids = podcastDao.savePodcasts(podcasts);
                returnInt = ids.length;

                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        return returnInt;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int rowsUpdated = 0;

        switch (uriMatcher.match(uri)) {
            case PODCAST:
                if (values.containsKey(COLUMN_NAME_FILE_URI)) {
                    rowsUpdated = podcastDao.setFileUri(Long.parseLong(selectionArgs[0]),
                            values.getAsString(COLUMN_NAME_FILE_URI));
                } else if (values.containsKey(COLUMN_NAME_STATE)) {
                    rowsUpdated = podcastDao.setPodcastState(Long.parseLong(selectionArgs[0]),
                            values.getAsInteger(COLUMN_NAME_STATE));
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // no-op
        return 0;
    }
}
