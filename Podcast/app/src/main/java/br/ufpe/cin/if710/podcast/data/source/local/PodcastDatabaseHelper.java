package br.ufpe.cin.if710.podcast.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PodcastDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "podcasts.db";

    private static final int DATABASE_VERSION = 1;

    private final static String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PodcastPersistenceContract.PodcastEntry.TABLE_NAME + " (" +
                    PodcastPersistenceContract.PodcastEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                    PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_DESCRIPTION + " TEXT NOT NULL, " +
                    PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_PUB_DATE + " TEXT NOT NULL, " +
                    PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_LINK + ", " +
                    PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_DOWNLOAD_LINK + " TEXT NOT NULL, " +
                    PodcastPersistenceContract.PodcastEntry.COLUMN_NAME_FILE_URI +
                    ")";

    public PodcastDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        throw new RuntimeException("inutilizado");
    }
}
