package br.ufpe.cin.if710.podcast.podcastdetail;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.data.source.LoaderProvider;

public class PodcastDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PODCAST_ID = "PODCAST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_detail);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        int podcastId = getIntent().getIntExtra(EXTRA_PODCAST_ID, 0);

        PodcastDetailFragment podcastDetailFragment =
                (PodcastDetailFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (podcastDetailFragment == null) {
            // Create the fragment
            podcastDetailFragment = new PodcastDetailFragment();

            // Add fragment to activity
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_frame, podcastDetailFragment);
            transaction.commit();
        }

        // Create the presenter
        new PodcastDetailPresenter(
                podcastId,
                new LoaderProvider(this),
                getSupportLoaderManager(),
                podcastDetailFragment
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
