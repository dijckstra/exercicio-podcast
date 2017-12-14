package br.ufpe.cin.if710.podcast.data.source;

import android.os.AsyncTask;

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
                // First, attempt to get data from network
                podcastsRemoteDataSource.getPodcasts(new GetPodcastsCallback() {
                    @Override
                    public void onPodcastsLoaded(List<Podcast> result) {
                        podcasts = result;
                    }
                });

                // If the retrieval was successful, update the local data
                if (podcasts != null) {
                    savePodcasts(podcasts);
                }

                return podcasts;
            }

            @Override
            protected void onPostExecute(List<Podcast> podcasts) {
                if (callback != null) {
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
    public void savePodcasts(List<Podcast> podcasts) {
        podcastsLocalDataSource.savePodcasts(podcasts);
    }

    @Override
    public void setPodcastUri(long podcastId, String uri) {
        podcastsLocalDataSource.setPodcastUri(podcastId, uri);
    }

    @Override
    public void setPodcastState(long podcastId, int state) {
        podcastsLocalDataSource.setPodcastState(podcastId, state);
    }
}
