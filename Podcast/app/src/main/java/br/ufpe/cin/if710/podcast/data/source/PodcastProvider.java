package br.ufpe.cin.if710.podcast.data.source;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import br.ufpe.cin.if710.podcast.data.source.local.PodcastDatabaseHelper;
import br.ufpe.cin.if710.podcast.data.source.local.PodcastPersistenceContract;

public class PodcastProvider extends ContentProvider {

    private static final String TAG = "PodcastProvider";

    private static final int PODCAST = 100;
    private static final int PODCAST_ITEM = 101;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private PodcastDatabaseHelper databaseHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PodcastPersistenceContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, PodcastPersistenceContract.PodcastEntry.TABLE_NAME, PODCAST);
        matcher.addURI(authority, PodcastPersistenceContract.PodcastEntry.TABLE_NAME + "/*", PODCAST_ITEM);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new PodcastDatabaseHelper(getContext());
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
                retCursor = databaseHelper.getReadableDatabase().query(
                        PodcastPersistenceContract.PodcastEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PODCAST_ITEM:
                String[] where = {uri.getLastPathSegment()};
                retCursor = databaseHelper.getReadableDatabase().query(
                        PodcastPersistenceContract.PodcastEntry.TABLE_NAME,
                        projection,
                        PodcastPersistenceContract.PodcastEntry._ID + " = ?",
                        where,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Uri returnUri = null;

        switch (uriMatcher.match(uri)) {
            case PODCAST:
                // Check if the item to be inserted exists.
                // Since the podcast information from the server doesn't
                // have a unique identifier, the selection is done
                // by passing the publication date
                Cursor exists = database.query(
                        PodcastPersistenceContract.PodcastEntry.TABLE_NAME,
                        new String[] { PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_TITLE },
                        PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_PUB_DATE + " = ?",
                        new String[] { values.getAsString(PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_PUB_DATE) },
                        null,
                        null,
                        null
                );

                // If it doesn't exist, insert it.
                if (!exists.moveToLast()) {
                    long _id = database.insert(
                            PodcastPersistenceContract.PodcastEntry.TABLE_NAME,
                            null,
                            values
                    );

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
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int rowsUpdated;

        switch (uriMatcher.match(uri)) {
            case PODCAST:
                rowsUpdated = database.update(
                        PodcastPersistenceContract.PodcastEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    // no-op
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int rowsDeleted;

        switch (uriMatcher.match(uri)) {
            case PODCAST:
                rowsDeleted = database.delete(
                        PodcastPersistenceContract.PodcastEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }
}
