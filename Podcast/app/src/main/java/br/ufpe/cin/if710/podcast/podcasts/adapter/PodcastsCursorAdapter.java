package br.ufpe.cin.if710.podcast.podcasts.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.data.Podcast;
import br.ufpe.cin.if710.podcast.podcasts.listener.PodcastItemListener;

import static br.ufpe.cin.if710.podcast.data.Podcast.STATE_DOWNLOADED;
import static br.ufpe.cin.if710.podcast.data.Podcast.STATE_NOT_DOWNLOADED;
import static br.ufpe.cin.if710.podcast.data.Podcast.STATE_PLAYING;

public class PodcastsCursorAdapter extends CursorAdapter {
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
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        final Podcast podcast = Podcast.from(cursor);
        viewHolder.titleTV.setText(podcast.getTitle());
        viewHolder.dateTV.setText(podcast.getPubDate());

        final ImageButton button = viewHolder.downloadB;
        button.setColorFilter(ContextCompat.getColor(context, android.R.color.black));

        switch (podcast.getState()) {
            case STATE_NOT_DOWNLOADED:
                button.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_download));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemListener.onDownloadPodcastClick(podcast);
                    }
                });
                break;

            case STATE_DOWNLOADED:
            button.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play));
            button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemListener.onPlayPodcastClick(podcast);
                    }
                });
            break;

            case STATE_PLAYING:
            button.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pause));
            button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemListener.onPausePodcastClick(podcast);
                    }
                });
            break;
        }

        viewHolder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.onPodcastClick(podcast);
            }
        });
    }

    private class ViewHolder {
        final View rowView;
        final TextView titleTV;
        final TextView dateTV;
        final ImageButton downloadB;

        ViewHolder(View view) {
            rowView = view;
            titleTV = (TextView) view.findViewById(R.id.item_title);
            dateTV = (TextView) view.findViewById(R.id.item_date);
            downloadB = (ImageButton) view.findViewById(R.id.item_action);
        }
    }
}
