package br.ufpe.cin.if710.podcast.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static br.ufpe.cin.if710.podcast.data.PodcastSQLContract.PodcastEntry.CREATE_CMD;

public class PodcastDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "podcasts";
    private static final int DATABASE_VERSION = 1;
    private static PodcastDBHelper db;

    private PodcastDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static PodcastDBHelper getInstance(Context c) {
        if (db == null) {
            db = new PodcastDBHelper(c.getApplicationContext());
        }

        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        throw new RuntimeException("inutilizado");
    }
}
