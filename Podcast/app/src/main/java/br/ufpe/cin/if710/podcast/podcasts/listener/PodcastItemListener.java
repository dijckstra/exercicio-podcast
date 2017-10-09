package br.ufpe.cin.if710.podcast.podcasts.listener;

import br.ufpe.cin.if710.podcast.data.Podcast;

public interface PodcastItemListener {
    void onPodcastClick(Podcast clickedPodcast);

    void onDownloadPodcastClick(Podcast podcast);
}
