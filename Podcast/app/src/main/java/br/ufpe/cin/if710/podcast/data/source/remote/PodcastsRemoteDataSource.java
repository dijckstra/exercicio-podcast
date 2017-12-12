package br.ufpe.cin.if710.podcast.data.source.remote;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.ufpe.cin.if710.podcast.data.Podcast;
import br.ufpe.cin.if710.podcast.data.source.PodcastsDataSource;
import br.ufpe.cin.if710.podcast.util.XmlFeedParser;

public class PodcastsRemoteDataSource implements PodcastsDataSource {

    private final String RSS_FEED = "http://leopoldomt.com/if710/fronteirasdaciencia.xml";

    private static final String TAG = "Podcasts";
    private static PodcastsRemoteDataSource INSTANCE;

    private PodcastsRemoteDataSource() {}

    public static PodcastsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PodcastsRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getPodcasts(final GetPodcastsCallback callback) {
        List<Podcast> itemList = null;

        try {
            itemList = XmlFeedParser.parse(getRssFeed(RSS_FEED));
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

        if (callback != null) {
            callback.onPodcastsLoaded(itemList);
        }
    }

    @Override
    public void savePodcast(Podcast podcast) {
        // since this is an RSS feed we are not manipulating data
    }

    @Override
    public void setPodcastUri(long podcastId, String uri) {
        // since this is an RSS feed we are not manipulating data
    }

    @Override
    public void setPodcastState(long podcastId, int state) {
        // since this is an RSS feed we are not manipulating data
    }

    //TODO Opcional - pesquise outros meios de obter arquivos da internet
    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";

        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }

            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return rssFeed;
    }
}
