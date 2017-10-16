package br.ufpe.cin.if710.podcast.data.source;

import android.content.Context;

import br.ufpe.cin.if710.podcast.data.source.local.PodcastsLocalDataSource;
import br.ufpe.cin.if710.podcast.data.source.remote.PodcastsRemoteDataSource;

/**
 * Enables injection of implementations for PodcastsDataSource.
 */
public class Repositories {
    public static PodcastsRepository getInstance(Context context) {
        return PodcastsRepository.getInstance(provideLocalDataSource(context), provideRemoteDataSource());
    }

    public static PodcastsRemoteDataSource provideRemoteDataSource() {
        return PodcastsRemoteDataSource.getInstance();
    }

    public static PodcastsLocalDataSource provideLocalDataSource(Context context) {
        return PodcastsLocalDataSource.getInstance(context.getContentResolver());
    }

}
