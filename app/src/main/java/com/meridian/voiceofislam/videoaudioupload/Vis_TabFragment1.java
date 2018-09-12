package com.meridian.voiceofislam.videoaudioupload;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
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

import com.meridian.voiceofislam.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by Libin_Cybraum on 8/19/2016.
 */
public class Vis_TabFragment1 extends Fragment implements View.OnClickListener {
    ImageView rec, gal, stop, sent,rec_icon;
    MediaRecorder recorder;
    File audiofile = null;
    static final String TAG = "MediaRecording";
    TextView textViewResponse;
    Uri newUri;
    String selectedFilePath;


    private static final int PICK_FILE_REQUEST = 1;
    TextView timer;

    long starttime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedtime = 0L;
    int t = 1;
    int secs = 0;
    int mins = 0;
    int milliseconds = 0;
    Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View views = inflater.inflate(R.layout.vis_tab_fragment_1, container, false);
        textViewResponse = (TextView)views. findViewById(R.id.textViewResponse);
        recorder = new MediaRecorder();
        rec_icon = (ImageView) views.findViewById(R.id.imageView3);
        rec = (ImageView) views.findViewById(R.id.imageView);
        gal = (ImageView) views.findViewById(R.id.imageView5);
        stop = (ImageView) views.findViewById(R.id.imageView1);
        sent = (ImageView) views.findViewById(R.id.imageView2);
        timer=(TextView)views.findViewById(R.id.textViewTime);
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starttime = 0L;
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updatedtime = 0L;
                t = 1;
                secs = 0;
                mins = 0;
                milliseconds = 0;
               timer.setText("Start");
                handler.removeCallbacks(updateTimer);
                timer.setText("00:00");
                recorder.stop();
                recorder.release();
                //after stopping the recorder, create the sound file and add it to media library.
                addRecordingToMediaLibrary();


            }
        });
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (t == 1) {

//timer will start
                    timer.setText("Pause");
                    starttime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimer, 0);
                    t = 0;
                } else {
//timer will pause
                    timer.setText("Start");
                    timer.setTextColor(Color.BLUE);
                    timeSwapBuff += timeInMilliseconds;
                    handler.removeCallbacks(updateTimer);
                    t = 1;
                }

                gal.setVisibility(View.GONE);
                rec.setBackgroundResource(R.drawable.rec_disabled);
                stop.setVisibility(View.VISIBLE);
                rec.setClickable(false);
                rec_icon.setBackgroundResource(R.drawable.ic_recd_mic);
                File dir = Environment.getExternalStorageDirectory();
                try {
                    audiofile = File.createTempFile("sound", ".mp4", dir);
       selectedFilePath=audiofile.toString();
                } catch (IOException e) {
                    Log.e(TAG, "external storage access error");
                    return;
                }
                //Creating MediaRecorder and specifying audio source, output format, encoder & output format
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setOutputFile(audiofile.getAbsolutePath());
                try {
                    recorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                recorder.start();
                stop.setVisibility(View.VISIBLE);
                gal.setVisibility(View.GONE);
            }
        });
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedFilePath==null){
                    Toast.makeText(getActivity(),"Select File",Toast.LENGTH_SHORT).show();
                }else {
                uploadVideo();
                rec.setBackgroundResource(R.drawable.ic_record);
                rec.setClickable(true);
                sent.setClickable(true);}
            }
        });

        return views;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("audio/*");
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
        }
    }


    private void uploadVideo() {
        class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploading = ProgressDialog.show(getActivity(), "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();
                textViewResponse.setText(Html.fromHtml("<b>Uploaded at <a href='" + s + "'>" + s + "</a></b>"));
                textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            protected String doInBackground(Void... params) {
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
    public void startRecording() throws IOException {
        //Creating file
        File dir = Environment.getExternalStorageDirectory();
        try {
            audiofile = File.createTempFile("sound", ".mp4", dir);

        } catch (IOException e) {
            Log.e(TAG, "external storage access error");
            return;
        }
        //Creating MediaRecorder and specifying audio source, output format, encoder & output format
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        recorder.prepare();
        recorder.start();
    }

    public void stopRecording() {
        //stopping recorder
        recorder.stop();
        recorder.release();
        //after stopping the recorder, create the sound file and add it to media library.
        addRecordingToMediaLibrary();
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

    public Runnable updateTimer = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - starttime;
            updatedtime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedtime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            milliseconds = (int) (updatedtime % 1000);
            timer.setText("" + mins + ":" + String.format("%02d", secs));
            timer.setTextColor(Color.RED);
            handler.postDelayed(this, 0);
        }};

}


