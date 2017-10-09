package br.ufpe.cin.if710.podcast.podcasts.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.data.Podcast;
import br.ufpe.cin.if710.podcast.podcasts.listener.PodcastItemListener;

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

    private class ViewHolder {
        final View rowView;
        final TextView titleTV;
        final TextView dateTV;
        final Button downloadB;

        ViewHolder(View view) {
            rowView = view;
            titleTV = view.findViewById(R.id.item_title);
            dateTV = view.findViewById(R.id.item_date);
            downloadB = view.findViewById(R.id.item_action);
        }
    }
}
