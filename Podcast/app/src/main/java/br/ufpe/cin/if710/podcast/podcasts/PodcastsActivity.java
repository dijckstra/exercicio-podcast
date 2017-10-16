package br.ufpe.cin.if710.podcast.podcasts;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.data.source.LoaderProvider;
import br.ufpe.cin.if710.podcast.data.source.Repositories;
import br.ufpe.cin.if710.podcast.util.PermissionsManager;

public class PodcastsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcasts);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestRequiredPermissions(this);

        PodcastsListFragment podcastsFragment =
                (PodcastsListFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame_list);
        if (podcastsFragment == null) {
            // Create the fragment
            podcastsFragment = new PodcastsListFragment();

            // Add fragment to activity
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_frame_list, podcastsFragment);
            transaction.commit();
        }

        // Create the presenter
        new PodcastsListPresenter(
                new LoaderProvider(this),
                getSupportLoaderManager(),
                Repositories.getInstance(getApplicationContext()),
                podcastsFragment,
                (DownloadManager) getSystemService(DOWNLOAD_SERVICE)
        );
    }

    private void requestRequiredPermissions(Context context) {
        PermissionsManager.requestPermissions(context);
    }
}
