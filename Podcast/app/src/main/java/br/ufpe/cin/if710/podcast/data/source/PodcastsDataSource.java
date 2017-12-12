package br.ufpe.cin.if710.podcast.data.source;

import java.util.List;

import br.ufpe.cin.if710.podcast.data.Podcast;

public interface PodcastsDataSource {

    interface GetPodcastsCallback {

        void onPodcastsLoaded(List<Podcast> podcasts);

    }

    void getPodcasts(GetPodcastsCallback callback);

    void savePodcast(Podcast podcast);

    void setPodcastUri(long podcastId, String uri);

    void setPodcastState(long podcastId, int state);
}
