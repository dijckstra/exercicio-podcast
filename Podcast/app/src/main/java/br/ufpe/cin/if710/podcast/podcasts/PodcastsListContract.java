package br.ufpe.cin.if710.podcast.podcasts;

import android.database.Cursor;

import br.ufpe.cin.if710.podcast.BaseView;
import br.ufpe.cin.if710.podcast.data.Podcast;

public class PodcastsListContract {

    interface View extends BaseView<Presenter> {
        void showPodcasts(Cursor data);

        void showToast(String text);

        void showPodcastDetailsUi(Podcast requestedPodcast);

        void showSettingsUi();

        void playMedia(Podcast podcast);

        void pauseMedia();
    }

    public interface Presenter {
        void initLoader();

        void loadPodcasts();

        void openPodcastDetails(Podcast requestedPodcast);

        void downloadPodcast(Podcast podcast);

        void playPodcast(Podcast podcast);

        void pausePodcast(Podcast podcast);
    }
}
