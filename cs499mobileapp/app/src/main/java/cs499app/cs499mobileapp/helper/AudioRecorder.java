package cs499app.cs499mobileapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.util.Output;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import cs499app.cs499mobileapp.MainActivity;
import cs499app.cs499mobileapp.Manifest;
import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.view.PlayerFragment;

import static cs499app.cs499mobileapp.service.MusicService.getEncodedURL;

public class AudioRecorder {


    private String currentUrl;
    private Context context;
    private Activity baseActivity;
    private OutputStream outstream;
    private InputStream inStream;
    private CircularSeekBar seekbar;
    private PlayerFragment playerFragmentRef;
    private int maxFileSizeInBytes;
    AsyncTask<Void, Integer, Void> recordAudioTask;

    public AudioRecorder(Context context, Activity activity, CircularSeekBar seekbar) {
        this.seekbar = seekbar;
        this.context = context;
        this.baseActivity = activity;
        currentUrl = "";
        setBaseDirectory();
    }



    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public void setBaseDirectory()
    {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(baseActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MainActivity.EXPORT_DB_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        else {
            Log.d("externalstorage", "path:" + Environment.getExternalStorageDirectory());
            Log.d("AudioRecorder:", "setDestinationDirectory");
            String rootAudioDir = context.getString(R.string.SAVE_DIRECTORY_NAME);
            String baseDir = context.getString(R.string.ROOT_DIRECTORY_NAME);
            File f = new File(Environment.getExternalStorageDirectory() + "/" + baseDir, rootAudioDir);
            if (!f.exists()) {
                Log.d("create rootdir", "creating");
                f.mkdirs();
            }
        }
    }

    public String getTimeStampedFileName(String stationName)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        sdf.setTimeZone(TimeZone.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return stationName +"_"+ currentDateandTime+".mp3";
    }

    public File setDestinationDirectory(String playlistName, String stationName)
    {
        String rootDir = context.getString(R.string.ROOT_DIRECTORY_NAME);
        String audioDir = context.getString(R.string.SAVE_DIRECTORY_NAME);

        File f1 = new File(Environment.getExternalStorageDirectory() + "/"
                + rootDir +"/" + audioDir +"/"
                + playlistName, stationName);
        if (!f1.exists()) {
            f1.mkdirs();
        }
        return f1;
    }

    public void startRecording(final String playlistName, final String stationName, final String stationUrl)
    {

        seekbar.setMax(maxFileSizeInBytes);
        seekbar.setProgress(0);
        recordAudioTask = new AsyncTask<Void, Integer, Void>() {

             @Override
             protected Void doInBackground(Void... voids) {

                 int bytesRead = 0;
                 int buffer;

                 setDestinationDirectory(playlistName,stationName);

                 File outputSource = new File(setDestinationDirectory(playlistName,stationName),
                         getTimeStampedFileName(stationName));
                 Log.d("OutputPath:",outputSource.getAbsolutePath());

//                 InputStream inputStream = null;
//                 FileOutputStream fileOutputStream = null;
                 URL url = null;
                 try {
                     Log.d("StationURL"," for Record:" + stationUrl);
                     url = new URL(getEncodedURL(stationUrl));
                      inStream  = url.openStream();
                      outstream = new FileOutputStream(outputSource);

                     while ((buffer = inStream.read()) != -1 && !isCancelled()) {
                         outstream.write(buffer);
                         bytesRead++;
                         if(bytesRead % 1000 == 0)
                         {
                             Log.d("AudioRecording", "bytesRead=" + bytesRead);
                             publishProgress(bytesRead);
                         }
                         if(bytesRead >= maxFileSizeInBytes)
                             inStream.close();
                     }
                 }
                  catch (FileNotFoundException e1) {
                     e1.printStackTrace();
                 }
                 catch (MalformedURLException e2) {
                     e2.printStackTrace();
                 } catch (IOException e3) {
                         e3.printStackTrace();
                 } catch (Exception e) {
                     e.printStackTrace();
                 } finally {

                 }

                 return null;
             }

             @Override
             protected void onPostExecute(Void aVoid) {


                 Handler handler = new Handler();
                 handler.postDelayed(new Runnable() {
                     public void run() {
                         // yourMethod();
                     }
                 }, 5000);
                 seekbar.setProgress(0);
                 playerFragmentRef.setRecordIconOff();

                 Log.d("post Execute"," from recording job");



             }

            @Override
            protected void onProgressUpdate(Integer... values) {
                seekbar.setProgress(values[0]);
            }

            @Override
            protected void onCancelled(Void aVoid) {
                Log.d("AudioRecording", "CANCELLED");
                seekbar.setProgress(0);

                try {
                    //inStream.close();
                    outstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        recordAudioTask.execute();
    }

    public void stopRecording()
    {
        if( recordAudioTask != null)
            if(!recordAudioTask.isCancelled())
                recordAudioTask.cancel(true);
    }

    public int getMaxFileSizeInBytes() {
        return maxFileSizeInBytes;
    }

    public void setMaxFileSizeInBytes(int maxFileSizeInBytes) {
        this.maxFileSizeInBytes = maxFileSizeInBytes;
    }

    public void setSeekbar(CircularSeekBar seekbar) {
        this.seekbar = seekbar;
    }

    public void setPlayerFragmentRef(PlayerFragment playerFragmentRef) {
        this.playerFragmentRef = playerFragmentRef;
    }
}
