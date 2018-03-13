package com.example.shoaib.user.cloud;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.Crashlytics;
import com.example.shoaib.user.R;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import io.fabric.sdk.android.Fabric;

import static android.Manifest.permission.INTERNET;
import static android.content.Intent.ACTION_VIEW;

public class cloudActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    // Activity Tag to call in Log command
    Uri videoUri;

    // boolean exit to exit application when back pressed three times
    private Boolean exit = false;

    // A view of dialog that ask to choose between upload or watch video
    MaterialDialog chooseDialog;

    AppCompatButton viewSavedVideosButton;
    AppCompatButton viewDriveActionButton;


    // Permission code called when write and read permission is triggered (SDK>Marshmallow)
    private static final int REQUEST_WRITE_PERMISSION = 786;


    private static final String TAG = "Google Drive Activity";
    private static final int REQUEST_CODE_RESOLUTION = 404;
    private static final int RECORD_REQUEST_CODE = 101;

    public static final int RequestPermissionCode = 7;
    private static final int REQUEST_CODE_OPENER = 4444;
    private DriveId mFileId;
    public DriveFile file;

    private GoogleApiClient mGoogleApiClient;
    private boolean fileOperation = false;
    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (result.getStatus().isSuccess()) {

                        Toast.makeText(getApplicationContext(), "file created: " + " " +
                                result.getDriveFile().getDriveId(), Toast.LENGTH_LONG).show();

                    }

                    return;

                }
            };

    /**
     * This is Result result handler of Drive contents.
     * this callback method call CreateFileOnGoogleDrive() method.
     */
    final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback;

    {
        driveContentsCallback = new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(DriveApi.DriveContentsResult result) {

                if (result.getStatus().isSuccess()) {
                    if (fileOperation == true) {

                        CreateFileOnGoogleDrive(result);

                    }
                }
            }
        };
    }

    public void CreateFileOnGoogleDrive(DriveApi.DriveContentsResult result) {

        final DriveContents driveContents = result.getDriveContents();

        // Perform I/O off the UI thread.
        new Thread() {
            @Override
            public void run() {
                // write content to DriveContents
                OutputStream outputStream = driveContents.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream);
                try {
                    writer.write("Hello abhay!");
                    writer.close();

                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }

                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                        .setTitle("abhaytest2")
                        .setMimeType("quot;text/plain")
                        .setStarred(true).build();

                // create a file in root folder
                Drive.DriveApi.getRootFolder(mGoogleApiClient)
                        .createFile(mGoogleApiClient, changeSet, driveContents).setResultCallback(fileCallback);

            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_cloud);

        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission

        } else {
            if (CheckingPermissionIsEnabledOrNot()) {

            } else {
                RequestMultiplePermission();
            }
        }

        if (mGoogleApiClient == null) {

            /**
             * Create the API client and bind it to an instance variable.
             * We use this instance as the callback for connection and connection failures.
             * Since no account name is passed, the user is prompted to choose.
             */
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        mGoogleApiClient.connect();
        fileOperation = false;

        // create new contents resource
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(driveContentsCallbacks);

    }




    private void createVideoRecordedFolder() {
        String folder_main = "Recorded Videos";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);

        if (!f.exists()) {
            Log.e(TAG, "folder Created");
            f.mkdirs();
        }

    }

    private void callFilePickerDialog() {

        // Set the properties of the File Dialog
        DialogProperties properties = new DialogProperties();

        // Selection mode of the file picked. In your case it is single file selection mode.
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;

        // The folder which will be shown when fileDialog is called. In your case it is Recorded videos folder
        properties.root = new File(Environment.getExternalStorageDirectory().getPath()
                + File.separator + "Recorded Videos" + File.separator);

        // Properties to deal with exceptions.
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);

        // Defining the extension of videos which the fileDialog will show to user.
        properties.extensions = new String[]{"mp4", "avi", "flv", "mov", "wmv"};

        // Creating Object of file Dialog and Setting the Title of it.
        FilePickerDialog dialog = new FilePickerDialog(cloudActivity.this, properties);
        dialog.setTitle("Select a Video File");

        // Called when user select on a video file. This function will get the Uri of the file you select from the
        // video list
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                Log.e(TAG, "" + files[0]);

                if (files.length != 0) {
                    videoUri = Uri.parse(files[0]);
                }

                if (videoUri != null) {
                    // Call a Dialog to choose whether to play video or upload it.
                    chooseDialog(videoUri);
                }

            }
        });

        // show the FileDialog to choose the video.
        dialog.show();
    }

    private void chooseDialog(final Uri uri) {

        chooseDialog = new MaterialDialog.Builder(this)
                .title("Video Selected Successfully !")
                .titleColorRes(R.color.colorPrimary)
                .content("Choose Whether to Play Video or Upload it.")
                .positiveText("Upload")
                .negativeText("Play")
                .positiveColorRes(R.color.colorAccent)
                .negativeColorRes(R.color.colorAccent)

                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {

                        // In case of Upload button is pressed, call the Upload Activity and pass the String to upload
                        // video activity on google drive.

                        if (!isDeviceOnline()) {
                            chooseDialog.dismiss();
                            showSnackBar();
                        } else {
                            Log.e(TAG, "upload to google drive");

                            // Intent is called to open another activity in Android
                            Intent intent = new Intent(cloudActivity.this, UploadVideo.class);

                            //called to pass value of uri from this activity to the next one
                            intent.putExtra("videoUri", String.valueOf(videoUri));

                            // used to start the activity mean hiding the current screen and showing/opening
                            //next one
                            startActivity(intent);

                        }

                    }
                })

                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {

                        Log.e(TAG, "play video");

                        //call play video function here and pass the uri to it.
                        watchVideo(uri);
                    }
                })
                .cancelable(true)
                .show();

    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    void showSnackBar() {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), "No internet connection.", Snackbar.LENGTH_LONG);


        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        snackbar.show();

    }

    private void watchVideo(Uri uri) {

        // Intent called to play video using video player which already exist in your phone.
        Intent playVideoIntent = new Intent(ACTION_VIEW);

        //tell intent the path where the video exist
        playVideoIntent.setDataAndType(uri, "video/*");

        // start the activity to play the video
        startActivity(playVideoIntent);
    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Log.e(TAG, "permission granted");

            createVideoRecordedFolder();
            callFilePickerDialog();

        } else {
            Log.e(TAG, "permission not granted");
        }
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
            startActivity(intent);
            finish();
            System.exit(0);
            // finish activity
        } else {
            Toast.makeText(getApplicationContext(), "Press Back again to leave.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    /* @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean InternetPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (InternetPermission) {

                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();


                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                break;
        }
    }*/
    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(cloudActivity.this, new String[]
                {
                        INTERNET
                }, RequestPermissionCode);

    }

    /**
     * Called when the activity will start interacting with the user.
     * At this point your activity is at the top of the activity stack,
     * with user input going to it.
     */

    @Override
    protected void onResume() {
        super.onResume();


        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission

        } else {
            if (CheckingPermissionIsEnabledOrNot()) {

            } else {
                RequestMultiplePermission();
            }
        }

        if (mGoogleApiClient == null) {

            /**
             * Create the API client and bind it to an instance variable.
             * We use this instance as the callback for connection and connection failures.
             * Since no account name is passed, the user is prompted to choose.
             */
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {

            // disconnect Google API client connection
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        // Called whenever the API client fails to connect.
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());

        if (!result.hasResolution()) {

            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }

        /**
         *  The failure has a resolution. Resolve it.
         *  Called typically when the app is not yet authorized, and an  authorization
         *  dialog is displayed to the user.
         */

        try {

            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);

        } catch (IntentSender.SendIntentException e) {

            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    /**
     * It invoked when Google API client connected
     *
     * @param connectionHint
     */
    @Override
    public void onConnected(Bundle connectionHint) {

        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
    }

    /**
     * It invoked when connection suspended
     *
     * @param cause
     */
    @Override
    public void onConnectionSuspended(int cause) {

        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    public void onClickCreateFile(View view) {

        fileOperation = true;
        // create new contents resource
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(driveContentsCallback);

    }

    public void onClickOpenFile(View view) {
        fileOperation = false;

        // create new contents resource
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(driveContentsCallbacks);
    }

    /**
     * This is Result result handler of Drive contents.
     * this callback method call CreateFileOnGoogleDrive() method
     * and also call OpenFileFromGoogleDrive() method, send intent onActivityResult() method to handle result.
     */
    ResultCallback<DriveApi.DriveContentsResult> driveContentsCallbacks =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {

                    if (result.getStatus().isSuccess()) {

                        if (fileOperation == true) {

                            CreateFileOnGoogleDrive(result);

                        } else {

                            OpenFileFromGoogleDrive();

                        }
                    }
                }
            };

    /**
     * Open list of folder and file of the Google Drive
     */
    public void OpenFileFromGoogleDrive() {

        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()

                .setActivityTitle("Select a video file")
                .setMimeType(new String[]{"video/mp4"})
                .build(mGoogleApiClient);
        try {
            startIntentSenderForResult(

                    intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);

        } catch (IntentSender.SendIntentException e) {
            Log.w(TAG, "Unable to send intent", e);
        }
    }


    /**
     * Handle Response of selected file
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_OPENER:

                if (resultCode == RESULT_OK) {

                    mFileId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    Log.e("file id", mFileId.getResourceId() + "");


                    String url = "https://drive.google.com/open?id=" + mFileId.getResourceId();
                    Intent i = new Intent(ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                } else if (resultCode == RESULT_CANCELED) {

                    finish();
                }

                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
