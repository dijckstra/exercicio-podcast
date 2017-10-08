package br.ufpe.cin.if710.podcast.podcasts;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import br.ufpe.cin.if710.podcast.data.Podcast;
import br.ufpe.cin.if710.podcast.data.source.LoaderProvider;
import br.ufpe.cin.if710.podcast.data.source.PodcastsDataSource;
import br.ufpe.cin.if710.podcast.data.source.PodcastsRepository;

public class PodcastsPresenter implements PodcastsContract.Presenter,
        PodcastsRepository.LoadDataCallback, LoaderManager.LoaderCallbacks<Cursor>,
        PodcastsDataSource.GetPodcastsCallback {

    public static final int PODCASTS_LOADER = 1;
    private static final String TAG = "PodcastsPresenter";

    private LoaderProvider loaderProvider;
    private LoaderManager loaderManager;
    private PodcastsRepository podcastsRepository;
    private PodcastsContract.View podcastsView;

    public PodcastsPresenter(LoaderProvider loaderProvider,
                             LoaderManager loaderManager,
                             PodcastsRepository podcastsRepository,
                             PodcastsContract.View podcastsView) {
        this.loaderProvider = loaderProvider;
        this.loaderManager = loaderManager;
        this.podcastsRepository = podcastsRepository;
        this.podcastsView = podcastsView;
        this.podcastsView.setPresenter(this);
    }

    // LoaderManager callbacks
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return loaderProvider.createPodcastsLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            if (data.moveToLast()) {
                onDataLoaded(data);
            }
        } else {
            onDataNotAvailable();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        onDataReset();
    }

    // Presenter contract methods
    @Override
    public void start() {
        Log.d(TAG, "start");
        loadPodcasts();
    }

    @Override
    public void loadPodcasts() {
        podcastsRepository.getPodcasts(this);
    }

    // Data source callbacks
    @Override
    public void onPodcastsLoaded(List<Podcast> podcasts) {
        // Since the CursorLoader will load the data,
        // the data from this callback can be ignored.
        Log.d(TAG, "onPodcastsLoaded");
        if (loaderManager.getLoader(PODCASTS_LOADER) == null) {
            loaderManager.initLoader(PODCASTS_LOADER, null, this);
        } else {
            loaderManager.restartLoader(PODCASTS_LOADER, null, this);
        }
    }

    @Override
    public void onDataNotAvailable() {
        // podcastsView.showLoadingPodcastsError();
    }

    // Load data callbacks
    @Override
    public void onDataLoaded(Cursor data) {
        podcastsView.showPodcasts(data);
    }

    @Override
    public void onDataReset() {
        podcastsView.showPodcasts(null);
    }
}
