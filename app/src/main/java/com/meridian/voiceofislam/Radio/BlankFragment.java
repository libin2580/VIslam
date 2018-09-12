package com.meridian.voiceofislam.Radio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.meridian.voiceofislam.R;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "AudioFxDemo";

    private static final float VISUALIZER_HEIGHT_DIP = 200f;
    private MediaPlayer mp;
    private Visualizer mVisualizer;
    private Equalizer mEqualizer;
    final String audioFile = "http://streams.museter.com:8078";
    private LinearLayout ll;
    private VisualizerView mVisualizerView;
    ImageButton implay;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v= inflater.inflate(R.layout.fragment_blank, container, false);
        ll= (LinearLayout) v.findViewById(R.id.ll);
        implay= (ImageButton) v.findViewById(R.id.implay);

        mp=new MediaPlayer();
        mp.start();

        System.out.println( "audiosessionid"+mp.getAudioSessionId());

        implay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("preparedlistener2");
              mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

                if(mp.isPlaying())
                {
                   mp.stop();
                    mVisualizer.setEnabled(false);

                    implay.setImageResource(R.drawable.pause_radio);

                }
                else
                {
                    mp.start();


                    try
                    {
                        mp.setDataSource(audioFile);
                       mp.prepareAsync();

                        mVisualizer.setEnabled(true);


                    }
                    catch (IOException e)
                    {   e.printStackTrace();
                        System.out.println("eroorrr" + e);
                    }
                    implay.setImageResource(R.drawable.play_radio);

                }
            }
        });
       mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared( MediaPlayer mp)
            {
                if(mp.isPlaying())
                {
                    mp.stop();
                    mVisualizer.setEnabled(false);

                }
                else
                {
                    mp.start();
                    mVisualizer.setEnabled(true);


                }


            }
        });



        Log.d(TAG, "MediaPlayer audio session ID: " + mp.getAudioSessionId());

        setupVisualizer(mp);
        mVisualizer.setEnabled(true);

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {

            @Override
            public void onCompletion(MediaPlayer arg0)
            {
                // TODO Auto-generated method stub
                mVisualizer.setEnabled(false);

            }

        });

        return  v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void setupVisualizer(MediaPlayer mp)
    {

        mVisualizerView=new VisualizerView(getActivity());
        mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                (int)(VISUALIZER_HEIGHT_DIP * getResources().getDisplayMetrics().density)));

        ll.addView(mVisualizerView);


        mVisualizer=new Visualizer(mp.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {

            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                              int samplingRate) {
                // TODO Auto-generated method stub
          mVisualizerView.updateVisualizer(bytes);

            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft,
                                         int samplingRate) {
                // TODO Auto-generated method stub

            }
        }, Visualizer.getMaxCaptureRate()/2, true,false);

    }

    public void setupEqualizer()
    { LinearLayout ll=new LinearLayout(getActivity());
        ll.setBackgroundResource(R.drawable.whitebak);
        mEqualizer=new Equalizer(0,mp.getAudioSessionId());
        mEqualizer.setEnabled(true);
        TextView tv=new TextView(getActivity());
        tv.setText("equalizer");
        ll.addView(tv);
        short bands=mEqualizer.getNumberOfBands();
        final short min=mEqualizer.getBandLevelRange()[0];
        final short max=mEqualizer.getBandLevelRange()[1];
        for(short i=0;i<bands;i++)
        {
            final short band=1;
            TextView tv1=new TextView(getActivity());
            tv1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            tv1.setGravity(Gravity.CENTER_HORIZONTAL);
            tv1.setText((mEqualizer.getCenterFreq(band)/1000)+"hz");
            ll.addView(tv1);
            LinearLayout lv=new LinearLayout(getActivity());
            lv.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.weight=1;
            SeekBar bar=new SeekBar(getActivity());
            bar.setLayoutParams(param);
            bar.setMax(max-min);
            bar.setProgress(mEqualizer.getBandLevel(band));
            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    // TODO Auto-generated method stub
                    mEqualizer.setBandLevel(band, (short)(progress+min));

                }
            });

            lv.addView(bar);
            ll.addView(lv);
        }

    }
//    class VisualizerView extends View {
//        private byte[] mBytes;
//        private float[] mPoints;
//        private Rect mRect = new Rect();
//
//        private Paint mForePaint = new Paint();
//
//        public VisualizerView(Context context) {
//            super(context);
//            init();
//        }
//
//        private void init() {
//            mBytes = null;
//            mForePaint.setStrokeWidth(2f);
//            mForePaint.setAntiAlias(true);
//            // mForePaint.setColor(Color.rgb(0, 128, 255));
//            mForePaint.setColor(Color.rgb(250, 158, 31));
//        }
//
//        public void updateVisualizer(byte[] bytes) {
//            mBytes = bytes;
//            invalidate();
//        }
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//            super.onDraw(canvas);
//
//            if (mBytes == null)
//            {
//                return;
//            }
//
//            if (mPoints == null || mPoints.length < mBytes.length * 4)
//            {
//                mPoints = new float[mBytes.length * 4];
//            }
//
//            mRect.set(0, 0, getWidth(), getHeight());
//
//            for (int i = 0; i < mBytes.length - 1; i++)
//            {
//                mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);
//                mPoints[i * 4 + 1] = mRect.height() / 2
//                        + ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
//                mPoints[i * 4 + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
//                mPoints[i * 4 + 3] = mRect.height() / 2
//                        + ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2) / 128;
//            }
//
//            canvas.drawLines(mPoints, mForePaint);
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (BlankFragment.this != null
                && getFragmentManager().findFragmentById(
              BlankFragment.this.getId()) != null) {

            getFragmentManager().beginTransaction().remove(BlankFragment.this)
                    .commit();

        }
    }


}
