package com.meridian.voiceofislam.videoaudioupload;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.meridian.voiceofislam.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
 * Created by Libin_Cybraum on 8/19/2016.
 */
public class Vis_TabFragment2 extends Fragment implements View.OnClickListener {
    ImageView rec, gal, stop, sent;


    //  MediaRecorder recorder;
    File audiofile = null;
    static final String TAG = "MediaRecording";
    TextView textViewResponse;
    Uri newUri;
    String selectedFilePath;
    View views;


    private static final int PICK_FILE_REQUEST = 1;


    // Activity request codes
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "";

    private Uri fileUri; // file url to store image/video

    private VideoView videoPreview;
    //File mediaFile;

    MediaRecorder recorder;
    File mediaFile;
    File mediaStorageDir;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View views = inflater.inflate(R.layout.vis_tab_fragment_2, container, false);
        textViewResponse = (TextView) views.findViewById(R.id.textViewResponse);
        recorder = new MediaRecorder();
        videoPreview = (VideoView) views.findViewById(R.id.videoPreview);
        rec = (ImageView) views.findViewById(R.id.imageView);
        gal = (ImageView) views.findViewById(R.id.imageView5);
        stop = (ImageView) views.findViewById(R.id.imageView1);
        sent = (ImageView) views.findViewById(R.id.imageView2);
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recorder.stop();
                recorder.release();
                //after stopping the recorder, create the sound file and add it to media library.
                addRecordingToMediaLibrary();


            }
        });
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rec.setBackgroundResource(R.drawable.rec_disabled);
                rec.setClickable(false);
                recordVideo();
            }
        });
        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getActivity(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            // finish();
        }

        /**
         * Checking device has camera hardware or not
         * */

        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedFilePath==null){
                    Toast.makeText(getActivity(),"Select File",Toast.LENGTH_SHORT).show();
                }else {
                    uploadVideo();
                    rec.setBackgroundResource(R.drawable.ic_record);
                    rec.setClickable(true);
                    sent.setClickable(true);
                }
            }
        });

        return views;
    }

    private boolean isDeviceSupportCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /*
 * Recording video
 */
    public void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        // 1- for high quality video
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }
    /**
     * Receiving activity result method will be called after closing the camera
     * */
  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // video successfully recorded
                // preview the recorded video
                previewVideo();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getActivity(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            }
            else if(requestCode == PICK_FILE_REQUEST){
                    if(data == null){
                        //no data present
                        return;
                    }


                    Uri selectedFileUri = data.getData();
                    selectedFilePath = Vis_FilePath.getPath(getActivity(),selectedFileUri);
                    Log.i(TAG,"Selected File Path:" + selectedFilePath);

                    if(selectedFilePath != null && !selectedFilePath.equals("")){
                        textViewResponse.setText(selectedFilePath);
                    }else{
                        Toast.makeText(getActivity(),"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else {
                // failed to record video
                Toast.makeText(getActivity(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }*/

    /*
     * Previewing recorded video
     */
    /**
     * Previewing recorded video
     */


    public void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("video/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Choose File to Vis_Upload.."),PICK_FILE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //no data present
                    return;
                }


                Uri selectedFileUri = data.getData();
                selectedFilePath = Vis_FilePath.getPath(getActivity(),selectedFileUri);
                Log.i(TAG,"Selected File Path:" + selectedFilePath);

                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    textViewResponse.setText(selectedFilePath);

                }else{
                    Toast.makeText(getActivity(),"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }

            }
            else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    textViewResponse.setText(fileUri.getPath());
                    selectedFilePath=fileUri.getPath();

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // user cancelled recording
                    Toast.makeText(getActivity(),
                            "User cancelled video recording", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // failed to record video
                    Toast.makeText(getActivity(),
                            "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }
    public void previewVideo() {
        try {
            // hide image preview
//videoPreview
            videoPreview.setVisibility(View.VISIBLE);
            videoPreview.setVideoPath(fileUri.getPath());

            selectedFilePath=fileUri.getPath();
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<start playing>>>>>>>>>>>>>>>>>>>>>>>>>" + selectedFilePath);
            // start playing
            videoPreview.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    public File getOutputMediaFile(int type) {

        // External sdcard location
      mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist


        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault()).format(new Date());

        if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID" + timeStamp + ".mp4");
            selectedFilePath=mediaFile.getPath();

        } else {
            return null;
        }

        return mediaFile;
    }

    public void uploadVideo() {
        class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            public void onPreExecute() {
                super.onPreExecute();
                uploading = ProgressDialog.show(getActivity(), "Uploading File", "Please wait...", false, false);
            }

            @Override
            public void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();
                textViewResponse.setText(Html.fromHtml("<b>Uploaded at <a href='" + s + "'>" + s + "</a></b>"));
                textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            public String doInBackground(Void... params) {
                Vis_Upload u = new Vis_Upload();

                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<md>>>>>>>>>>>>>>>>>>>>>>>>>" + selectedFilePath);
                String msg = u.uploadVideo(selectedFilePath);
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<mdeeeeeeeeeeeeeeeeeee>>>>>>>>>>>>>>>>>>>>>>>>>" + msg);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }




    public void addRecordingToMediaLibrary() {
        //creating content values of size 4
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());

        //creating content resolver and storing it in the external content uri
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        newUri = contentResolver.insert(base, values);
        selectedFilePath = Vis_FilePath.getPath(getActivity(),newUri);
        //sending broadcast message to scan the media file so that it can be available
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        Toast.makeText(getActivity(), "Added File " + newUri, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View view) {

    }
}


