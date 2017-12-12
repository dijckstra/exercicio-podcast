package br.ufpe.cin.if710.podcast.podcastdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.data.Podcast;

public class PodcastDetailFragment extends Fragment implements PodcastDetailContract.View {

    private static final String TAG = "PodcastDetailFragment";

    private PodcastDetailContract.Presenter presenter;

    private TextView detailTitle;
    private TextView detailDescription;
    private TextView detailPubDate;
    private TextView detailDownloadLink;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_podcast_detail, container, false);

        detailTitle = (TextView) root.findViewById(R.id.podcast_detail_title);
        detailDescription = (TextView) root.findViewById(R.id.podcast_detail_description);
        detailPubDate = (TextView) root.findViewById(R.id.podcast_detail_pub_date);
        detailDownloadLink = (TextView) root.findViewById(R.id.podcast_detail_link);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadPodcast();
    }

    // View contract methods
    @Override
    public void setPresenter(PodcastDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPodcastDetails(Podcast podcast) {
        detailTitle.setText(podcast.getTitle());
        detailDescription.setText(podcast.getDescription());
        detailPubDate.setText(podcast.getPubDate());
        detailDownloadLink.setText(podcast.getDownloadLink());
    }
}
