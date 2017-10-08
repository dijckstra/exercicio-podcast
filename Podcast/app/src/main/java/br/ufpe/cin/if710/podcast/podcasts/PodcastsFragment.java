package br.ufpe.cin.if710.podcast.podcasts;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.data.Podcast;

public class PodcastsFragment extends Fragment implements PodcastsContract.View {

    private static final String TAG = "PodcastsFragment";

    private PodcastsContract.Presenter presenter;
    private PodcastsCursorAdapter listAdapter;

    // Listener for clicks on podcasts in the ListView.
    private PodcastItemListener listener = new PodcastItemListener() {
        @Override
        public void onPodcastClick(Podcast clickedPodcast) {
            // presenter.openPodcastDetails(clickedPodcast);
        }

        @Override
        public void onDownloadPodcastClick(Podcast podcast) {
            // presenter.downloadPodcast(podcast);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_podcasts, container, false);

        Log.d(TAG, "onCreateView");

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
                // showSettingsUi();
                break;
            case R.id.action_outro:
                break;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        presenter.start();
    }

    @Override
    public void setPresenter(PodcastsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPodcasts(Cursor podcasts) {
        listAdapter.swapCursor(podcasts);

    }

    public interface PodcastItemListener {

        void onPodcastClick(Podcast clickedPodcast);

        void onDownloadPodcastClick(Podcast podcast);
    }

    private static class PodcastsCursorAdapter extends CursorAdapter {

        private PodcastItemListener itemListener;

        public PodcastsCursorAdapter(Context context, PodcastItemListener itemListener) {
            super(context, null, 0);
            this.itemListener = itemListener;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.itemlista, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();

            final Podcast podcast = Podcast.from(cursor);
            viewHolder.titleTV.setText(podcast.getTitle());
            viewHolder.dateTV.setText(podcast.getPubDate());
            viewHolder.downloadB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onDownloadPodcastClick(podcast);
                }
            });
            viewHolder.rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onPodcastClick(podcast);
                }
            });
        }

        public static class ViewHolder {
            public final View rowView;
            public final TextView titleTV;
            public final TextView dateTV;
            public final Button downloadB;

            public ViewHolder(View view) {
                rowView = view;
                titleTV = view.findViewById(R.id.item_title);
                dateTV = view.findViewById(R.id.item_date);
                downloadB = view.findViewById(R.id.item_action);
            }
        }
    }
}
