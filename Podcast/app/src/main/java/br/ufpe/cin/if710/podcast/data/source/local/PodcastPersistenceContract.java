package br.ufpe.cin.if710.podcast.data.source.local;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import br.ufpe.cin.if710.podcast.BuildConfig;

public class PodcastPersistenceContract {

    private static final String CONTENT_SCHEME = "content://";
    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID;
    public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_SCHEME + CONTENT_AUTHORITY);
    // MIME type for a collection of items
    public static final String CONTENT_PODCAST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + CONTENT_AUTHORITY + "/" + PodcastEntry.TABLE_NAME;
    // MIME type for a specific item
    public static final String CONTENT_PODCAST_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + CONTENT_AUTHORITY + "/" + PodcastEntry.TABLE_NAME;

    private PodcastPersistenceContract() {}

    public static class PodcastEntry implements BaseColumns {
        public static final String COLUMN_NAME_ENTRY_ID = "entry_id";
        public static final String TABLE_NAME = "episodes";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PUB_DATE = "pub_date";
        public static final String COLUMN_NAME_LINK = "link";
        public static final String COLUMN_NAME_DOWNLOAD_LINK = "download_link";
        public static final String COLUMN_NAME_FILE_URI = "download_uri";
        // Table URI
        public static final Uri CONTENT_PODCAST_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static String[] PODCAST_COLUMNS = new String[] {
                _ID,
                COLUMN_NAME_ENTRY_ID,
                COLUMN_NAME_TITLE,
                COLUMN_NAME_DESCRIPTION,
                COLUMN_NAME_PUB_DATE,
                COLUMN_NAME_LINK,
                COLUMN_NAME_DOWNLOAD_LINK,
                COLUMN_NAME_FILE_URI
        };

        public static Uri buildPodcastsUri() {
            return CONTENT_PODCAST_URI.buildUpon().build();
        }

        public static Uri buildPodcastsUriWith(int id) {
            return CONTENT_PODCAST_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        public static Uri buildPodcastsUriWith(long id) {
            return ContentUris.withAppendedId(CONTENT_PODCAST_URI, id);
        }
    }
}