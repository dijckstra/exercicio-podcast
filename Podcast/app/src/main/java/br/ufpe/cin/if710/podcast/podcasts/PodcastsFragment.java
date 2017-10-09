package br.ufpe.cin.if710.podcast.podcasts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import br.ufpe.cin.if710.podcast.settings.SettingsActivity;

public class PodcastsFragment extends Fragment implements PodcastsContract.View {

    private static final String TAG = "PodcastsFragment";

    private PodcastsContract.Presenter presenter;
    private PodcastsCursorAdapter listAdapter;

    // Listener for clicks on podcasts in the ListView.
    private PodcastItemListener listener = new PodcastItemListener() {
        @Override
        public void onPodcastClick(Podcast clickedPodcast) {
            presenter.openPodcastDetails(clickedPodcast);
        }

        @Override
        public void onDownloadPodcastClick(Podcast podcast) {
            // presenter.downloadPodcast(podcast);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_podcasts, container, false);

        setHasOptionsMenu(true);

        // Set up podcasts view
        listAdapter = new PodcastsCursorAdapter(getActivity(), listener);
        ListView listView = root.findViewById(R.id.podcasts_list);
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
        presenter.start();
    }

    // View contract methods
    @Override
    public void setPresenter(PodcastsContract.Presenter presenter) {
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

}
