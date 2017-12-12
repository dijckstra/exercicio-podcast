package br.ufpe.cin.if710.podcast.receivers;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import br.ufpe.cin.if710.podcast.data.source.Repositories;

import static br.ufpe.cin.if710.podcast.data.Podcast.STATE_DOWNLOADED;

public class DownloadCompleteReceiver extends BroadcastReceiver {

    public static final String TAG = "DownloadReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Query for the completed download
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);

        Cursor c = downloadManager.query(query);
        if (c.moveToLast()) {
            // Check if it was successful
            int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                // Get the path and the id associated with the downloaded podcast
                columnIndex = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                String path = c.getString(columnIndex);
                long id = Long.parseLong(path.substring(path.lastIndexOf('/') + 1, path.indexOf('.')));

                // Update the data sources with the path to the downloaded podcast
                Repositories.getInstance(context).setPodcastUri(id, path);
                Repositories.getInstance(context).setPodcastState(id, STATE_DOWNLOADED);
            }
        }

        c.close();
    }

}
