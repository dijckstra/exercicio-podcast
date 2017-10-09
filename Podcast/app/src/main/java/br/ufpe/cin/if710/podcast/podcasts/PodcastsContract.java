package br.ufpe.cin.if710.podcast.podcasts;

import android.database.Cursor;

import br.ufpe.cin.if710.podcast.BasePresenter;
import br.ufpe.cin.if710.podcast.BaseView;
import br.ufpe.cin.if710.podcast.data.Podcast;

public class PodcastsContract {

    interface View extends BaseView<Presenter> {
        void showPodcasts(Cursor data);

        void showToast(String text);

        void showPodcastDetailsUi(Podcast requestedPodcast);

        void showSettingsUi();
    }

    interface Presenter extends BasePresenter {
        void loadPodcasts();

        void openPodcastDetails(Podcast requestedPodcast);
    }
}
