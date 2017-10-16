package br.ufpe.cin.if710.podcast.podcasts;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.List;

import br.ufpe.cin.if710.podcast.data.Podcast;
import br.ufpe.cin.if710.podcast.data.source.LoaderProvider;
import br.ufpe.cin.if710.podcast.data.source.PodcastsDataSource;
import br.ufpe.cin.if710.podcast.data.source.PodcastsRepository;

import static br.ufpe.cin.if710.podcast.data.Podcast.STATE_DOWNLOADED;
import static br.ufpe.cin.if710.podcast.data.Podcast.STATE_PLAYING;

public class PodcastsListPresenter implements PodcastsListContract.Presenter,
        LoaderManager.LoaderCallbacks<Cursor>, PodcastsDataSource.GetPodcastsCallback {

    public static final int PODCASTS_LOADER = 1;
    private static final String TAG = "PodcastsListPresenter";

    private LoaderProvider loaderProvider;
    private LoaderManager loaderManager;
    private PodcastsRepository podcastsRepository;
    private PodcastsListContract.View podcastsView;

    private DownloadManager downloadManager;

    public PodcastsListPresenter(LoaderProvider loaderProvider,
                                 LoaderManager loaderManager,
                                 PodcastsRepository podcastsRepository,
                                 PodcastsListContract.View podcastsView,
                                 DownloadManager downloadManager) {
        this.loaderProvider = loaderProvider;
        this.loaderManager = loaderManager;
        this.podcastsRepository = podcastsRepository;
        this.podcastsView = podcastsView;
        this.downloadManager = downloadManager;

        this.podcastsView.setPresenter(this);
    }

    // Presenter contract methods
    public void initLoader() {
        // Initialize or restart the CursorLoader. This will
        // create a CursorLoader in the onCreateLoader() method
        // and fetch the data from the local database, after which
        // the onLoadFinished() method is called.
        if (loaderManager.getLoader(PODCASTS_LOADER) == null) {
            loaderManager.initLoader(PODCASTS_LOADER, null, this);
        } else {
            loaderManager.restartLoader(PODCASTS_LOADER, null, this);
        }
    }

    @Override
    public void loadPodcasts() {
        podcastsRepository.getPodcasts(this);
    }

    @Override
    public void openPodcastDetails(Podcast requestedPodcast) {
        podcastsView.showPodcastDetailsUi(requestedPodcast);
    }

    @Override
    public void downloadPodcast(Podcast podcast) {
        // Create a DownloadManager.Request instance
        Request request = new Request(Uri.parse(podcast.getDownloadLink()));
        request.setTitle(podcast.getTitle())
                .setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, podcast.getFileName());

        // Start the download
        downloadManager.enqueue(request);
    }

    @Override
    public void playPodcast(Podcast podcast) {
        podcastsRepository.setPodcastState(podcast.getId(), STATE_PLAYING);

        podcastsView.playMedia(podcast);
    }

    @Override
    public void pausePodcast(Podcast podcast) {
        podcastsRepository.setPodcastState(podcast.getId(), STATE_DOWNLOADED);
        podcastsView.pauseMedia();
    }

    // Data source callbacks
    @Override
    public void onPodcastsLoaded(List<Podcast> podcasts) {
        // Since the CursorLoader will load the data,
        // it can be ignored. If it's null, it means
        // there was an error fetching data from the RSS feed
        initLoader();
        if (podcasts == null) {
            podcastsView.showToast("Could not fetch data from network");
        }
    }

    // LoaderManager callbacks
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return loaderProvider.createPodcastsLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Refresh the view with the data
        if (data != null) {
            if (data.moveToLast()) {
                podcastsView.showPodcasts(data);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        podcastsView.showPodcasts(null);
    }
}
