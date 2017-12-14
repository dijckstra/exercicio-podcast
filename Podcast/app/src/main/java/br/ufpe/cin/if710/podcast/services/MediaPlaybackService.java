package br.ufpe.cin.if710.podcast.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.io.IOException;

import br.ufpe.cin.if710.podcast.PodcastApplication;
import br.ufpe.cin.if710.podcast.data.source.Repositories;

import static br.ufpe.cin.if710.podcast.data.Podcast.STATE_DOWNLOADED;
import static br.ufpe.cin.if710.podcast.data.Podcast.STATE_NOT_DOWNLOADED;

public class MediaPlaybackService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {

    private static final String TAG = "MediaPlaybackService";
    public static final String ACTION_PLAY_PAUSE_MEDIA = "br.ufpe.cin.if710.action.PLAY_PAUSE_MEDIA";

    // Binder given to clients
    private final IBinder iBinder = new Binder();
    private MediaPlayer mediaPlayer;

    private long podcastId;
    private String file;

    //Used to pause/resume MediaPlayer
    private BroadcastReceiver playPauseMediaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            toggleMedia();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            //An audio file is passed to the service through putExtra()
            file = intent.getExtras().getString("media");
            podcastId = intent.getExtras().getLong("podcastId");
        } catch (NullPointerException e) {
            stopSelf();
        }

        if (file != null && !file.isEmpty()) {
            initMediaPlayer();
        }

        registerReceiver(playPauseMediaReceiver, new IntentFilter(ACTION_PLAY_PAUSE_MEDIA));

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(playPauseMediaReceiver);

        Repositories.getInstance(this).setPodcastState(podcastId, STATE_DOWNLOADED);

        if (mediaPlayer != null) {
            stopMedia();
            mediaPlayer.release();
        }

        super.onDestroy();

        RefWatcher refWatcher = PodcastApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }

    private void initMediaPlayer() {
        Log.d(TAG, "initMediaPlayer");
        mediaPlayer = new MediaPlayer();
        //Set up MediaPlayer event listeners
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        //Reset so that the MediaPlayer is not pointing to another data source
        mediaPlayer.reset();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            // Set the data source to the file location
            mediaPlayer.setDataSource(file);
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }
        mediaPlayer.prepareAsync();
    }

    private void toggleMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        } else {
            mediaPlayer.pause();
        }
    }

    private void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void stopMedia() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //Invoked when playback of a media source has completed.
        stopMedia();
        //stop the service
        stopSelf();

        // Delete file from storage
        File audioFile = new File(file);
        boolean deleted = audioFile.delete();

        if (deleted) {
            Log.d(TAG, "deleted");
            Repositories.getInstance(this).setPodcastState(podcastId, STATE_NOT_DOWNLOADED);
            Repositories.getInstance(this).setPodcastUri(podcastId, "");
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //Invoked when the media source is ready for playback.
        mediaPlayer.seekTo(mp.getDuration() - 60000);
        Log.d(TAG, "onPrepared");
        playMedia();
    }

}
