package br.ufpe.cin.if710.podcast.podcasts;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.data.source.LoaderProvider;
import br.ufpe.cin.if710.podcast.data.source.Repositories;

public class PodcastsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcasts);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PodcastsFragment podcastsFragment =
                (PodcastsFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (podcastsFragment == null) {
            // Create the fragment
            podcastsFragment = new PodcastsFragment();

            // Add fragment to activity
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_frame, podcastsFragment);
            transaction.commit();
        }

        new PodcastsPresenter(
                new LoaderProvider(this),
                getSupportLoaderManager(),
                Repositories.providePodcastsRepository(getApplicationContext()),
                podcastsFragment
        );
    }
}
