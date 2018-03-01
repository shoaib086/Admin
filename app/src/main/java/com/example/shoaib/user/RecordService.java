package com.example.shoaib.user;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;


/*
*  A service class that will record screen video in background thread
* */
public class RecordService extends Service {

    // Initialization of MediaProjection and other components
    private MediaProjection mediaProjection;
    private MediaRecorder mediaRecorder;
    private VirtualDisplay virtualDisplay;

    //Initialization of video size and dpi
    private boolean running;
    private int width = 720;
    private int height = 1080;
    private int dpi;


    /**
     * A client is binding to the service with bindService()
     */
    @Override
    public IBinder onBind(Intent intent) {
        return new RecordBinder();
    }

    /**
     * The service is starting, due to a call to startService()
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * Called when the service is being created.
     * It initializes the mediaRecorder
     */
    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread serviceThread = new HandlerThread("service_thread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        serviceThread.start();
        running = false;
        mediaRecorder = new MediaRecorder();
    }


    /*
    *  Is called if the service is destroyed either by android to get memory or by its own service class.
    * */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //Instantiate mediaProjection with its object
    public void setMediaProject(MediaProjection project) {
        mediaProjection = project;
    }

    // A function that return whether the service is running or not
    public boolean isRunning() {
        return running;
    }

    // A function that configure the width, height and dpi of the screen.
    public void setConfig(int width, int height, int dpi) {
        this.width = width;
        this.height = height;
        this.dpi = dpi;
    }

    /*
    * This function start the process of recording screen and call necessary function to start it
    *  initRecorder is called to initialize the RecorderMedia,
    *  virtual display is called to Start the video input.
    * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean startRecord() {
        if (mediaProjection == null || running) {
            return false;
        }

        initRecorder();
        createVirtualDisplay();
        mediaRecorder.start();
        running = true;
        return true;
    }

    /*
    *  stopRecord function is used here to stop the recording of the screen and reset the media recorder and release
    *  the virtual display so if the user start the screening again it can be used again.
    * */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean stopRecord() {
        if (!running) {
            return false;
        }
        running = false;
        mediaRecorder.stop();
        mediaRecorder.reset();
        virtualDisplay.release();
        mediaProjection.stop();

        return true;
    }

    // Start the video input.
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createVirtualDisplay() {
        virtualDisplay = mediaProjection.createVirtualDisplay("MainScreen", width, height, dpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(), null, null);
    }

    /*
    * Initialize the VideoRecorder with the video size, the dpi of video, the name and path of the
    * video file (.mp4), the frame rate of file and the Bit rate at which video will be recorded and
    * at last the media recorder will be prepared to be able to play when start() is called.
    * */
    private void initRecorder() {
        //  mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);

        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(getsaveDirectory() + "video" + System.currentTimeMillis() + ".mp4");

        mediaRecorder.setVideoSize(width, (int) (height * 0.75));
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

        //  mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
        mediaRecorder.setVideoFrameRate(30);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This function set the directory if not exist and return the path where the video would be saved.
    public String getsaveDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Recorded Videos" + "/";

            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }

            return rootDir;
        } else {
            return null;
        }
    } 

    // Theis class bind the service with the MainActivity which mean if activity stop service stops too.
    public class RecordBinder extends Binder {
        public RecordService getRecordService() {
            return RecordService.this;
        }
    }
}


