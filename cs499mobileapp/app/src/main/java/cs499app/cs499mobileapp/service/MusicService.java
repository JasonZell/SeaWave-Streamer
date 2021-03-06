package cs499app.cs499mobileapp.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import cs499app.cs499mobileapp.R;

import static java.security.AccessController.getContext;


public class MusicService extends Service implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnBufferingUpdateListener{



    enum myPlayerState {
        Stopped, //MediaPlayer stopped
        Prepared, //MediaPlayer is prepared
        Playing, //MediaPlayer is playing
        Paused, //MediaPlayer is paused
        PlayerPaused,
        Resetted
    }

    MediaPlayer myPlayer = null;
    myPlayerState playerState;
    String currentURL;


    private BroadcastReceiver musicToggleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(getString(R.string.MUSIC_PLAY_PAUSE_TOGGLE)))
            {
                Log.e("MUSIC RECEIVER IN MUSIC", "MUSIC TOGGLED");
                if(myPlayer.isPlaying())
                {
                    playerState = myPlayerState.PlayerPaused;
                    myPlayer.pause();
                }
                else
                {
                    myPlayer.start();
                    playerState = myPlayerState.Playing;
                }

            }
        }
    };



    private BroadcastReceiver musicStopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(getString(R.string.MUSIC_ACTION_STOP)))
            {

                if(playerState == myPlayerState.Playing) {
                    Log.e("MUSIC RECEIVER IN MUSIC", "MUSIC STOPPED");
                    myPlayer.stop();
                    playerState = myPlayerState.Stopped;
                }

            }
        }
    };

    private BroadcastReceiver musicResetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(getString(R.string.MUSIC_ACTION_RESET)))
            {

                if(playerState == myPlayerState.Playing) {
                    Log.e("MUSIC RECEIVER IN MUSIC", "MUSIC RESETTED");
                    myPlayer.reset();
                    currentURL = "";
                    playerState = myPlayerState.Resetted;
                }
            }
        }
    };

    private BroadcastReceiver musicPauseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("INSIDE PAUSED RECEIVER","INSIDE PAUSED REVEICER");
            if(intent.getAction().equals(getString(R.string.MUSIC_ACTION_PAUSE)))
            {
                Log.e("MUSIC RECEIVER IN MUSIC", "MUSIC PAUSED");
                if(playerState == myPlayerState.Playing) {

                    myPlayer.pause();
                    playerState = myPlayerState.Paused;
                }

            }
        }
    };

    private BroadcastReceiver musicPlayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(getString(R.string.MUSIC_ACTION_PLAY)))
            {
                Log.e("MUSIC RECEIVER IN MUSIC", "MUSIC PLAYED");
//                if(playerState == myPlayerState.Paused) {
//
//                    myPlayer.start();
//                    playerState = myPlayerState.Playing;
//                }
//                else //if(playerState == myPlayerState.Stopped)
//                {

                    try {
                        myPlayer.reset();
                        if(currentURL =="") {
                            Toast.makeText(context, "Cannot Play Empty URL", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        myPlayer.setDataSource(getEncodedURL(currentURL));
                        //myPlayer.prepare();
                        myPlayer.prepareAsync();
                    } catch (IOException ex)
                    {
                        Toast.makeText(context, "MUSIC PREPARATION FAILED", Toast.LENGTH_SHORT).show();
                        Log.e("MUSIC","PREPARATION FAILED");
                        ex.printStackTrace();
                    } catch (Exception e) {
                            e.printStackTrace();
                        }
                    //myPlayer.start();
                   // playerState = myPlayerState.Playing;
               // }

            }
        }
    };

    private BroadcastReceiver musicPlayUrlReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("INSIDE PLAYURL RECEIVER","INSIDE PLAY URL REVEICER");
            if(intent.getAction().equals(getString(R.string.MUSIC_ACTION_PLAY_URL)))
            {
                currentURL = intent.getStringExtra(getString(R.string.MUSIC_URL_TO_PLAY));
                Log.i("currentURL to PLAY:",currentURL);
                Log.e("MUSIC RECEIVER IN MUSIC", "MUSIC PLAYING URL");
                myPlayer.reset();
                try {
                    if(currentURL =="")
                    {
                        Toast.makeText(context, "Cannot Play Empty URL", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    myPlayer.setDataSource(getEncodedURL(currentURL));
                    //myPlayer.prepare();
                    myPlayer.prepareAsync();
                } catch (IOException ex)
                {
                    Toast.makeText(context, "MUSIC PREPARATION FAILED", Toast.LENGTH_SHORT).show();
                    Log.e("MUSIC","PREPARATION FAILED");
                    ex.printStackTrace();
                }
                catch(URISyntaxException uriexception)
                {
                    Toast.makeText(context, "Invalid URL!", Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Toast.makeText(context, "MUSIC EXCECPTION IN PLAY URL", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }



            }
        }
    };

    //Method: onStartCommand
    //Purpose: method is called when the service is started
    //setup all the receivers that the service is associated with
    //
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Music receiver
        registerReceiver(musicToggleReceiver,new IntentFilter(getString(R.string.MUSIC_PLAY_PAUSE_TOGGLE)),getString(R.string.BROADCAST_PRIVATE),null);
        registerReceiver(musicPlayReceiver,new IntentFilter(getString(R.string.MUSIC_ACTION_PLAY)),getString(R.string.BROADCAST_PRIVATE),null);
        registerReceiver(musicStopReceiver,new IntentFilter(getString(R.string.MUSIC_ACTION_STOP)),getString(R.string.BROADCAST_PRIVATE),null);
        registerReceiver(musicPauseReceiver,new IntentFilter(getString(R.string.MUSIC_ACTION_PAUSE)),getString(R.string.BROADCAST_PRIVATE),null);
        registerReceiver(musicPlayUrlReceiver,new IntentFilter(getString(R.string.MUSIC_ACTION_PLAY_URL)),getString(R.string.BROADCAST_PRIVATE),null);
        registerReceiver(musicResetReceiver,new IntentFilter(getString(R.string.MUSIC_ACTION_RESET)),getString(R.string.BROADCAST_PRIVATE),null);

        Log.d("INSISDE MUSIC SERVICE","MUSIC SERVICE");
        currentURL ="";
        if (intent.getAction().equals(getString(R.string.MUSIC_ACTION_CREATE))) {
            Log.e("MUSIC","MUSIC_ACTION_CREATE FIRED");

            if(myPlayer == null) {
                {
                    // myPlayer = MediaPlayer.create(this, R.raw.lullaby);
                    //AssetFileDescriptor musicAsset = getResources().openRawResourceFd(R.raw.lullaby);
                    final String url = "http://174.36.206.197:8000";
                    String url2= "http://s3.voscast.com:8456";
                    myPlayer = new MediaPlayer();
                    try {
                        //myPlayer.setDataSource(musicAsset.getFileDescriptor(),musicAsset.getStartOffset(),musicAsset.getLength());
                       // myPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        myPlayer.setAudioAttributes(new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build());
                       // myPlayer.setDataSource(getEncodedURL(url));
//                    } catch (IOException e) {
//                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    myPlayer.setOnPreparedListener(this);
                    myPlayer.setOnErrorListener(this);



                    // myPlayer.prepareAsync();

//                    try {
//                        myPlayer.prepare(); // see onPrepared()
//
//                    } catch (IOException e) {
//                        Toast.makeText(this, "MUSIC PREPARATION FAILED!!!", Toast.LENGTH_SHORT).show();
//                        Log.e("MUSIC","PREPARATION FAILED");
//                        e.printStackTrace();
//                    }

                   // Log.e("MUSIC","created and prepared player");

//                    MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
//                    try {
//                        metaRetriever.setDataSource(getEncodedURL(url));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    String artist =  metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
//                    String title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
//                    if(artist != null)
//                    Log.e("Artist:",artist);
//                    if(title != null)
//                        Log.e("Title:",title);
//


//                    Log.e("extracting","Metadata..");
//                    FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
//                    mmr.setDataSource(url);
//                    //mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
//                    String ar = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE);
//                    if(ar != null)
//                        Log.e("Artist:",ar);
//
//
//                    AsyncTask<Void, Void, Void> loadURLTask = new AsyncTask<Void, Void, Void>() {
//
//                        @Override
//                        protected Void doInBackground(Void... voids) {
//                            try {
//                                IcyStreamMeta icy = new IcyStreamMeta(new URL("http://174.36.206.197:8000"));
//                                Log.e("ICYTitle:", icy.getTitle());
//                                Log.e("ICYArtist:", icy.getArtist());
//
//
//                            } catch (MalformedURLException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            return null;
//                        }
//                    };
//                    loadURLTask.execute();


                }
            }
        }
        return START_NOT_STICKY; // service started but will not restart when it is destroyed
    }

    static public String getEncodedURL(String urlStr) throws Exception
    {
        URL url = new URL(urlStr);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        urlStr = uri.toASCIIString();
        return urlStr;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        if(what == MediaPlayer.MEDIA_ERROR_UNKNOWN)
        {
            Toast.makeText(this, "MEDIA_ERROR_UNKNOWN", Toast.LENGTH_SHORT).show();
        }
        else if(what == MediaPlayer.MEDIA_ERROR_SERVER_DIED)
        {
            Toast.makeText(this, "MEDIA_ERROR_SERVER_DIED", Toast.LENGTH_SHORT).show();

        }else if(what == MediaPlayer.MEDIA_ERROR_IO){
            Toast.makeText(this, "MEDIA_ERROR_IO", Toast.LENGTH_SHORT).show();


        }else if(what == MediaPlayer.MEDIA_ERROR_MALFORMED){
            Toast.makeText(this, "MEDIA_ERROR_MALFORMED", Toast.LENGTH_SHORT).show();

        }else if(what == MediaPlayer.MEDIA_ERROR_UNSUPPORTED){
            Toast.makeText(this, "MEDIA_ERROR_UNSUPPORTED", Toast.LENGTH_SHORT).show();

        }else if(what == MediaPlayer.MEDIA_ERROR_TIMED_OUT){
            Toast.makeText(this, "MEDIA_ERROR_TIMED_OUT", Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        if(i == MediaPlayer.MEDIA_INFO_METADATA_UPDATE)
        {
            Log.e("MEDIAPLAYER","METADATA UPDATED!!!!");

            return true;
        }
        return false;
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        //myPlayer.setLooping(true);
        Log.e("MUSIC SERVICE:", "ON PREPARED LISTENER Called,PLAYING");
        playerState = myPlayerState.Playing;
        // myPlayer.start();
        mp.start();
    }


    //Method: onDestroy
    //Purpose: unregister receiver and release music player resource
    @Override
    public void onDestroy()
    {
        if(myPlayer != null)
        {
            myPlayer.release();
        }
        myPlayer = null;

        //destroy music broadcast receiver
        unregisterReceiver(musicToggleReceiver);
        unregisterReceiver(musicPauseReceiver);
        unregisterReceiver(musicStopReceiver);
        unregisterReceiver(musicPlayReceiver);
        unregisterReceiver(musicPlayUrlReceiver);
        unregisterReceiver(musicResetReceiver);



    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }





}
