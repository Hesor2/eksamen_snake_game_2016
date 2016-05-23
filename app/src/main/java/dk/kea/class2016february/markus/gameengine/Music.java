package dk.kea.class2016february.markus.gameengine;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

public class Music implements MediaPlayer.OnCompletionListener
{
    private MediaPlayer mediaPlayer;
    private boolean isPrepared = false;

    public Music(AssetFileDescriptor assetFileDescriptor)
    {
        mediaPlayer = new MediaPlayer();
        try
        {
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e)
        {
            throw new RuntimeException("Could not load music file");
        }
    }

    public void dispose()
    {
        if (mediaPlayer.isPlaying()) mediaPlayer.stop();
        mediaPlayer.release();
    }

    public boolean isLooping()
    {
        return mediaPlayer.isLooping();
    }

    public boolean isPlaying()
    {
        return mediaPlayer.isPlaying();
    }

    public boolean isStopped()
    {
        return !isPrepared;
    }

    public void pause()
    {
        if(mediaPlayer.isPlaying()) mediaPlayer.pause();
    }

    public void play()
    {
        if(mediaPlayer.isPlaying()) return;
        try
        {
            synchronized (this)
            {
                if(!isPrepared) mediaPlayer.prepare();
                mediaPlayer.start();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void start()
    {
        if(mediaPlayer.isPlaying()) pause();
        try
        {
            synchronized (this)
            {
                if(!isPrepared) mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setLooping(boolean isLooping)
    {
        mediaPlayer.setLooping(isLooping);
    }

    public void setVolume(float volume)
    {
        mediaPlayer.setVolume(volume, volume);
    }

    public void setVolume(float lVolume, float rVolume)
    {
        mediaPlayer.setVolume(lVolume, rVolume);
    }

    public void stop()
    {
        synchronized (this)
        {
            if(!isPrepared) return;
            mediaPlayer.stop();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        synchronized (this)
        {
            isPrepared = false;
        }
    }
}
