package br.ufpe.cin.if710.podcast.podcastdetail;

import br.ufpe.cin.if710.podcast.BaseView;
import br.ufpe.cin.if710.podcast.data.Podcast;

public class PodcastDetailContract {

    interface View extends BaseView<Presenter> {

        void showPodcastDetails(Podcast podcast);

    }

    interface Presenter {

        void loadPodcast();

    }
}
