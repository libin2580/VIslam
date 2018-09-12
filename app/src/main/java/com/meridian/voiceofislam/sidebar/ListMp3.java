package com.meridian.voiceofislam.sidebar;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.meridian.voiceofislam.Constant;
import com.meridian.voiceofislam.R;
import com.meridian.voiceofislam.audioplayer.Constants;
import com.meridian.voiceofislam.audioplayer.NotificationService;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class ListMp3 extends AppCompatActivity {
    ListView lv;

    String audio;
    public    static MediaPlayer mediaPlayer;
    private SeekBar seekBar;
 public   static ImageView implay,impause;
    String path= Constant.BASE_URL+"uploads/audio/";
    TextView totalTime,currentTime;
    ImageView mImageView;


    SlidingUpPanelLayout     mLayout;
   RelativeLayout cntrl;
    String audioFile;
 public static   ImageView downplay,imforward,imbackward;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list_mp3);
        lv = (ListView)findViewById(R.id.filelist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_topa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        final ArrayList<String> FilesInFolder = GetFiles(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/VoiceOfIslam_Downloads");
        cntrl = (RelativeLayout) findViewById(R.id.dragView);
        cntrl.setTag("cntrl");

        imforward= (ImageView)findViewById(R.id.forward);
        downplay= (ImageView)findViewById(R.id.downplay);
        imbackward= (ImageView)findViewById(R.id.backward);
        implay= (ImageView)findViewById(R.id.play);
        impause= (ImageView)findViewById(R.id.pause);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        totalTime = (TextView)findViewById(R.id.totalTime);

        currentTime = (TextView) findViewById(R.id.currentTime);



        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        imforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int seekForwardTime = 5000;

                // get current song position
                int currentPosition = mediaPlayer.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if(currentPosition + seekForwardTime <= mediaPlayer.getDuration()){
                    // forward song
                    mediaPlayer.seekTo(currentPosition + seekForwardTime);
                }else{
                    // forward to end position
                    mediaPlayer.seekTo(mediaPlayer.getDuration());
                }

            }
        });
        imbackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set seek time
                int seekBackwardTime = 5000;

                // get current song position
                int currentPosition = mediaPlayer.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if(currentPosition - seekBackwardTime >= 0){
                    // forward song
                    mediaPlayer.seekTo(currentPosition - seekBackwardTime);
                }else{
                    // backward to starting position
                    mediaPlayer.seekTo(0);
                }

            }
        });
        downplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(mediaPlayer.isPlaying())
                {
                    implay.setBackgroundResource(R.drawable.newplay);
                    mediaPlayer.pause();
                    downplay.setBackgroundResource(R.drawable.ic_action_play);

                }
                else
                {

                    implay.setBackgroundResource(R.drawable.new_pause);
                    mediaPlayer.start();
                    downplay.setBackgroundResource(R.drawable.ic_action_pause);
                    Intent serviceIntent = new Intent(ListMp3.this, NotificationService.class);
                    serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                   startService(serviceIntent);


                }

            }
        });



        implay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {        implay.setBackgroundResource(R.drawable.newplay);

                    mediaPlayer.pause();
                    downplay.setBackgroundResource(R.drawable.ic_action_play);


                }
                else
                {   implay.setBackgroundResource(R.drawable.new_pause);
                    mediaPlayer.start();
                    downplay.setBackgroundResource(R.drawable.ic_action_pause);
                    Intent serviceIntent = new Intent(ListMp3.this, NotificationService.class);
                    serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(serviceIntent);
                }
            }
        });

        mImageView = (ImageView)findViewById(R.id.coverImage);




        final String coverImage ="https://dl.dropboxusercontent.com/u/2763264/RSS%20MP3%20Player/img3.jpg";





        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(final MediaPlayer mp)
            {


                //start media player
                mp.start();

                // link seekbar to bar view


                //update seekbar
                mRunnable.run();

            }
        });


        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

         TextView emptyText = (TextView)findViewById(android.R.id.empty);

if(FilesInFolder!=null) {
    UsersAdapter usersAdapter = new UsersAdapter(ListMp3.this, FilesInFolder);
    lv.setAdapter(usersAdapter);



}
        else {
    lv.setEmptyView(emptyText);

}


        Picasso.with(ListMp3.this).load(coverImage).into(mImageView);


    }
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if(mediaPlayer != null) {

                //set max value
                int mDuration = mediaPlayer.getDuration();
                seekBar.setMax(mDuration);

                //update total time text view

                totalTime.setText(getTimeString(mDuration));

                //set progress to current position
                int mCurrentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(mCurrentPosition);

                //update current time text view

                currentTime.setText(getTimeString(mCurrentPosition));

                //handle drag on seekbar
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(mediaPlayer != null && fromUser){
                            mediaPlayer.seekTo(progress);
                        }
                    }
                });


            }

            //repeat above code every second
            mHandler.postDelayed(this, 10);
        }
    };

    public ArrayList<String> GetFiles(String DirectoryPath)
    {
        ArrayList<String> MyFiles = new ArrayList<String>();
        File f = new File(DirectoryPath);

        f.mkdirs();
        File[] files = f.listFiles();
        if (files!= null)
            for (int i=0; i<files.length; i++) {
                MyFiles.add(files[i].getName());
                System.out.println("files[i].getName()" + files[i].getName());
            }



        return MyFiles;
    }
    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        long hours = millis / (1000*60*60);
        long minutes = ( millis % (1000*60*60) ) / (1000*60);
        long seconds = ( ( millis % (1000*60*60) ) % (1000*60) ) / 1000;

        buf
                .append(String.format("%02d", hours))
                .append(":")
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }
    public static void deleteFiles(String path) {

        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) { }
        }
    }

public class UsersAdapter extends ArrayAdapter<String> {
    ArrayList<String> files;
    Context context;
    public UsersAdapter(Context context, ArrayList<String> files) {
        super(context, 0, files);
        this.files=files;
        this.context=context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvitem = (TextView) convertView.findViewById(R.id.text_download_item);
        audioFile =files.get(position);
        tvitem.setText(audioFile);
        TextView tvdelet = (TextView) convertView.findViewById(R.id.delet);
        tvitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"itemmm",Toast.LENGTH_SHORT).show();
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                mediaPlayer.reset();

                Intent serviceIntent = new Intent(ListMp3.this, NotificationService.class);
                serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(serviceIntent);


                try {

                    mediaPlayer.setDataSource(path + audioFile);
                    System.out.println("path" + path + "/" + audioFile);
                    downplay.setBackgroundResource(R.drawable.ic_action_pause);
                    implay.setBackgroundResource(R.drawable.new_pause);
                    // mediaPlayer.prepare();


                    mediaPlayer.prepareAsync();


                    // might take long! (for buffering, etc)
                } catch (IOException e) {
                    e.printStackTrace();


                    System.out.println("#############################");
                }

            }
        });


        tvdelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //  deleteFiles(path+audioFile);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/VoiceOfIslam_Downloads",audioFile);
                boolean deleted = file.delete();
                Toast.makeText(getApplicationContext(),audioFile+" is deleted Successfully",Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());

            }
        });
        // Populate the data into the template view using the data object

        // Return the completed view to render on screen
        return convertView;
    }
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
