package br.ufpe.cin.if710.podcast.podcasts;

import android.database.Cursor;

import br.ufpe.cin.if710.podcast.BasePresenter;
import br.ufpe.cin.if710.podcast.BaseView;

public class PodcastsContract {

    interface View extends BaseView<Presenter> {
        void showPodcasts(Cursor data);
    }

    interface Presenter extends BasePresenter {
        void loadPodcasts();
    }
}
