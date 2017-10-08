package br.ufpe.cin.if710.podcast.data.source;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.data.Podcast;

public class PodcastsRepository implements PodcastsDataSource {

    private static final String TAG = "PodcastsRepository";
    private static PodcastsRepository INSTANCE = null;

    private PodcastsDataSource podcastsLocalDataSource, podcastsRemoteDataSource;

    private PodcastsRepository(PodcastsDataSource podcastsLocalDataSource,
                              PodcastsDataSource podcastsRemoteDataSource) {
        this.podcastsLocalDataSource = podcastsLocalDataSource;
        this.podcastsRemoteDataSource = podcastsRemoteDataSource;
    }

    public static PodcastsRepository getInstance(PodcastsDataSource podcastsLocalDataSource,
                                                 PodcastsDataSource podcastsRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new PodcastsRepository(podcastsLocalDataSource, podcastsRemoteDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void getPodcasts(final GetPodcastsCallback callback) {
        new AsyncTask<Void, Void, List<Podcast>>() {

            List<Podcast> podcasts = null;

            @Override
            protected List<Podcast> doInBackground(Void... params) {
                podcastsRemoteDataSource.getPodcasts(new GetPodcastsCallback() {
                    @Override
                    public void onPodcastsLoaded(List<Podcast> result) {
                        refreshLocalDataSource(result);
                        podcasts = result;
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });

                return podcasts;
            }

            @Override
            protected void onPostExecute(List<Podcast> podcasts) {
                if (podcasts != null && !podcasts.isEmpty()) {
                    callback.onPodcastsLoaded(podcasts);
                }
            }
        }.execute();
    }

    @Override
    public void savePodcast(Podcast podcast) {
        podcastsLocalDataSource.savePodcast(podcast);
    }

    @Override
    public void deleteAllPodcasts() {
        podcastsLocalDataSource.deleteAllPodcasts();
    }

    private void refreshLocalDataSource(List<Podcast> podcasts) {
        podcastsLocalDataSource.deleteAllPodcasts();

        for (Podcast podcast : podcasts) {
            podcastsLocalDataSource.savePodcast(podcast);
        }
    }

    public interface LoadDataCallback {
        void onDataLoaded(Cursor data);

        void onDataNotAvailable();

        void onDataReset();
    }
}
