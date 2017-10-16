package br.ufpe.cin.if710.podcast.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import br.ufpe.cin.if710.podcast.data.source.Repositories;

import static br.ufpe.cin.if710.podcast.data.Podcast.STATE_DOWNLOADED;
import static br.ufpe.cin.if710.podcast.data.Podcast.STATE_NOT_DOWNLOADED;

public class MediaPlaybackService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {

    private static final String TAG = "MediaPlaybackService";
    public static final String ACTION_PAUSE_MEDIA = "br.ufpe.cin.if710.action.PAUSE_MEDIA";

    // Binder given to clients
    private final IBinder iBinder = new Binder();
    private MediaPlayer mediaPlayer;

    private int podcastId;
    private String file;
    //Used to pause/resume MediaPlayer

    private BroadcastReceiver pauseMediaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseMedia();
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
            podcastId = intent.getExtras().getInt("podcastId");
        } catch (NullPointerException e) {
            stopSelf();
        }

        if (file != null && !file.isEmpty()) {
            initMediaPlayer();
        }

        registerReceiver(pauseMediaReceiver, new IntentFilter(ACTION_PAUSE_MEDIA));

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(pauseMediaReceiver);

        Repositories.getInstance(this).setPodcastState(podcastId, STATE_DOWNLOADED);

        if (mediaPlayer != null) {
            stopMedia();
            mediaPlayer.release();
        }

        super.onDestroy();
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

    private void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        mediaPlayer.seekTo(1800000);
    }

    private void stopMedia() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    private void pauseMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
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
            Repositories.getInstance(this).removePodcastLocalUri(podcastId);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //Invoked when the media source is ready for playback.
        Log.d(TAG, "onPrepared");
        playMedia();
    }

}
