package br.ufpe.cin.if710.podcast.podcastdetail;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import br.ufpe.cin.if710.podcast.data.Podcast;
import br.ufpe.cin.if710.podcast.data.source.LoaderProvider;

public class PodcastDetailPresenter implements PodcastDetailContract.Presenter,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PODCAST_LOADER = 2;

    private int podcastId;
    private LoaderProvider loaderProvider;
    private LoaderManager loaderManager;
    private PodcastDetailContract.View podcastDetailView;

    private Podcast podcast;

    public PodcastDetailPresenter(int podcastId,
                                  LoaderProvider loaderProvider,
                                  LoaderManager loaderManager,
                                  PodcastDetailContract.View podcastDetailView) {
        this.podcastId = podcastId;
        this.loaderProvider = loaderProvider;
        this.loaderManager = loaderManager;
        this.podcastDetailView = podcastDetailView;
        this.podcastDetailView.setPresenter(this);
    }

    // Presenter contract methods
    @Override
    public void start() {
        openPodcast();
    }

    private void openPodcast() {
        loaderManager.initLoader(PODCAST_LOADER, null, this);
    }

    // LoaderManager callbacks
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return loaderProvider.createPodcastLoader(podcastId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            if (data.moveToLast()) {
                onDataLoaded(data);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    // Load data
    private void onDataLoaded(Cursor data) {
        podcast = Podcast.from(data);
        podcastDetailView.showPodcastDetails(podcast);
    }

}
