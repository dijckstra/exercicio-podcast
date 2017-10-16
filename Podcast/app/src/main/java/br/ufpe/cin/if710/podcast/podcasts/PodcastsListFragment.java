package br.ufpe.cin.if710.podcast.podcasts;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.data.Podcast;
import br.ufpe.cin.if710.podcast.podcastdetail.PodcastDetailActivity;
import br.ufpe.cin.if710.podcast.podcasts.adapter.PodcastsCursorAdapter;
import br.ufpe.cin.if710.podcast.podcasts.listener.PodcastItemListener;
import br.ufpe.cin.if710.podcast.services.MediaPlaybackService;
import br.ufpe.cin.if710.podcast.settings.SettingsActivity;

import static br.ufpe.cin.if710.podcast.services.MediaPlaybackService.ACTION_PAUSE_MEDIA;

public class PodcastsListFragment extends Fragment implements PodcastsListContract.View {

    private static final String TAG = "PodcastsListFragment";

    private PodcastsListContract.Presenter presenter;
    private PodcastsCursorAdapter listAdapter;
    private ListView listView;

//    private MediaPlaybackService playerService;
    private boolean serviceBound;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

//            MediaPlaybackService.LocalBinder binder = (MediaPlaybackService.LocalBinder) service;
//            playerService = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    // Listener for clicks on podcasts in the ListView.
    private PodcastItemListener listener = new PodcastItemListener() {
        @Override
        public void onPodcastClick(Podcast clickedPodcast) {
            presenter.openPodcastDetails(clickedPodcast);
        }

        @Override
        public void onDownloadPodcastClick(Podcast podcast) {
            presenter.downloadPodcast(podcast);
        }

        @Override
        public void onPlayPodcastClick(Podcast podcast) {
            presenter.playPodcast(podcast);
        }

        @Override
        public void onPausePodcastClick(Podcast podcast) {
            presenter.pausePodcast(podcast);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_podcasts, container, false);

        setHasOptionsMenu(true);

        // Set up podcasts view
        listAdapter = new PodcastsCursorAdapter(getActivity(), listener);
        listView = (ListView) root.findViewById(R.id.podcasts_list);
        listView.setAdapter(listAdapter);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                showSettingsUi();
                break;
            case R.id.action_outro:
                break;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.initLoader();

        presenter.loadPodcasts();
    }

    @Override
    public void onDestroy() {
        if (serviceBound) {
            getContext().unbindService(serviceConnection);
            //service is active
//            playerService.stopSelf();
        }

        super.onDestroy();
    }

    // View contract methods
    @Override
    public void setPresenter(PodcastsListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPodcasts(Cursor podcasts) {
        listAdapter.swapCursor(podcasts);

    }

    @Override
    public void showPodcastDetailsUi(Podcast requestedPodcast) {
        Intent intent = new Intent(getContext(), PodcastDetailActivity.class);
        intent.putExtra(PodcastDetailActivity.EXTRA_PODCAST_ID, requestedPodcast.getId());
        startActivity(intent);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSettingsUi() {
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void playMedia(Podcast podcast) {
        if (!serviceBound) {
            Intent playerIntent = new Intent(getContext(), MediaPlaybackService.class);
            playerIntent.putExtra("media", podcast.getFileUri());
            playerIntent.putExtra("podcastId", podcast.getId());
            getContext().startService(playerIntent);
            getContext().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void pauseMedia() {
        Intent pauseIntent = new Intent(ACTION_PAUSE_MEDIA);
        getContext().sendBroadcast(pauseIntent);
    }

}
