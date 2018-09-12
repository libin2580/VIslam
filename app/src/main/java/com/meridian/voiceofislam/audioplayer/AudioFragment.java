package com.meridian.voiceofislam.audioplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.meridian.voiceofislam.Constant;
import com.meridian.voiceofislam.NetworkCheckingClass;
import com.meridian.voiceofislam.R;
import com.meridian.voiceofislam.RecyclerItemClickListener;
import com.meridian.voiceofislam.permission.CheckAndRequestPermission;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AudioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioFragment extends Fragment implements ExpandableListView.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String  REGISTER_URL  =Constant.BASE_URL+"services/response.php";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int lastExpandedPosition = -1;
    ArrayList<AudioFilterModel> arrayList ;
    List<String> listDataHeader;

    HashMap<String, List<String>> listDataChild;
    HashMap<String ,List<String>>listchild;
    ArrayList<AudioFilterContentModel> subCategoryModelArrayList2;
    ExpandableListAdapter expandableListAdapter;
    String audio;
    int i=0;
    public static MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    public  static ImageView implay,imforward,imbackward;
    static TextView totalTime,currentTime,now_playing_audio;
    public static   ImageView mImageView,downplay,minimizeplay,pauseeplay;
    static  RecyclerView rv,tab_rv;
    MKLoader progress;
    static List<AudioModel> filteredlistclick;
    static ArrayList<AudioModel> audioModelArrayList;
    RVAdapterAudioList rvAdapterAudioList;
    public  static   SlidingUpPanelLayout     mLayout;
    public   static RelativeLayout cntrl,bottomplay;
    String thumbnail;
    static ExpandableListView expandableListView;
    static   String audioFile;
    private FloatingSearchView mSearchView;
    private OnFragmentInteractionListener mListener;
    ImageView options;
    TextView category;
    RVAdapterFilterAudioContent rvAdapterFilterAudioContent;
    RVAdapterFilterAudio rvAdapterFilterAudio;
    String cat_id,sub_cat_id,filter_id="1";
    static FrameLayout framelayout_forclick;
    String url3 = Constant.BASE_URL+"services/response.php?fid=12&cat_id=1&subcat_id=1&filter_id="+filter_id+"&filter_status=filter";;
    SliderLayout sliderShow;
    CheckAndRequestPermission cr;
   static SwipeRefreshLayout swiperefreshlayout;
    LinearLayout slide_layout;
    public AudioFragment() {
        // Required empty public constructor
    }
    // DBHelper dbHelper;

    // TODO: Rename and change types and number of parameters
    public static AudioFragment newInstance() {
        AudioFragment fragment = new AudioFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    Point p;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //  Inflate the layout for this fragment
        final View v= inflater.inflate(R.layout.fragment_video_gallery, container, false);
        cr=new CheckAndRequestPermission();
        if(cr.checkAndRequestPermissions(getActivity()))
        {

        }
        else{
            /*Toast.makeText(getActivity(),"Please allow read and write permisiion for proper working of this app",Toast.LENGTH_SHORT).show();*/
        }
        slide_layout=(LinearLayout)v.findViewById(R.id.slide_layout);

        progress = (MKLoader) v. findViewById(R.id.progress_bar);
        mSearchView = (FloatingSearchView) v.findViewById(R.id.floating_search_view);
        mLayout = (SlidingUpPanelLayout)  v.findViewById(R.id.sliding_layout);
        bottomplay=(RelativeLayout)  v.findViewById(R.id.bottomplay);
        now_playing_audio = (TextView)v. findViewById(R.id.now_playing_text);
        framelayout_forclick=(FrameLayout)v.findViewById(R.id.framelayout_forclick);

        swiperefreshlayout=(SwipeRefreshLayout)v.findViewById(R.id.swiperefreshlayout);
        swiperefreshlayout.setColorSchemeResources(
                R.color.colorPrimary
        );
        cntrl = (RelativeLayout)v.findViewById(R.id.dragView);
        cntrl.setTag("cntrl");
        rv = (RecyclerView)v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        tab_rv=(RecyclerView)v.findViewById(R.id.tab_rv);
        LinearLayoutManager llmv = new LinearLayoutManager(getActivity());
        llmv.setOrientation(LinearLayoutManager.VERTICAL);
        tab_rv.setLayoutManager(llmv);

        expandableListView= (ExpandableListView) v.findViewById(R.id.expandableListView);
        implay= (ImageView)v.findViewById(R.id.play);
        imforward= (ImageView)v.findViewById(R.id.forward);
        downplay= (ImageView)v.findViewById(R.id.downplay);
        minimizeplay=(ImageView)v.findViewById(R.id.minimizeplay);
        pauseeplay=(ImageView)v.findViewById(R.id.pauseeplay);
        imbackward= (ImageView)v.findViewById(R.id.backward);
        seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        totalTime = (TextView)v. findViewById(R.id.totalTime);
        mImageView = (ImageView)v. findViewById(R.id.coverImage);
        currentTime = (TextView)v. findViewById(R.id.currentTime);
        category= (TextView) v.findViewById(R.id.category);

        options= (ImageView) v.findViewById(R.id.img_menu);
        prepareListData();
        expandableListView.setVisibility(View.GONE);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        sliderShow = (SliderLayout)v. findViewById(R.id.slider);
        framelayout_forclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableListView.setVisibility(View.GONE);
                framelayout_forclick.setVisibility(View.GONE);
            }
        });


        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NetworkCheckingClass networkCheckingClass1 = new NetworkCheckingClass(getActivity());
                boolean is = networkCheckingClass1.ckeckinternet();
                if (is)
                {
                   new swipeLoadData().execute();
                }

            }
        });
        func_banner();

        NetworkCheckingClass networkCheckingClass1 = new NetworkCheckingClass(getActivity());
        boolean is = networkCheckingClass1.ckeckinternet();
        if (is)
        { framelayout_forclick.setVisibility(View.GONE);
            expandableListView.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("responseeeee1"+response);
                            framelayout_forclick.setVisibility(View.GONE);

                            audioModelArrayList =new ArrayList<>();
                            AudioModel audioModel;

                            try {
                                ArrayList<String> newarray=new ArrayList<>();
                                JSONArray jsonArray=new JSONArray(response);
                                for(int i=0;i< jsonArray.length();i++)
                                {

                                    JSONObject jsonobject =jsonArray.getJSONObject(i);
                                    audioModel =new AudioModel();

                                    String category_id = jsonobject.getString("category_id");
                                    String sub_category_id = jsonobject.getString("sub_category_id");
                                    audio = jsonobject.getString("audio");
                                    thumbnail = jsonobject.getString("thumbnail");
                                    String Orator = jsonobject.getString("orator_name");
                                    String  title = jsonobject.getString("title");
                                    String Subject = jsonobject.getString("Subject");
                                    String Programme = jsonobject.getString("programme_name");
                                    String NEW = jsonobject.getString("new");
                                    newarray.add(NEW);
                                    audioModel.setCategory_id(category_id);
                                    System.out.println("xxx"+audio);
                                    audioModel.setSub_category_id(sub_category_id);
                                    audioModel.setAudio(audio);
                                    audioModel.setOrator(Orator);
                                    audioModel.setTitle(title);
                                    audioModel.setNEWW(NEW);

                                    audioModel.setProgramme(Programme);
                                    audioModel.setSubject(Subject);
                                    audioModel.setThumbnail(thumbnail);

                                    audioModelArrayList.add(audioModel);



                                }

                                filteredlistclick= audioModelArrayList;
                                rvAdapterAudioList = new RVAdapterAudioList(filteredlistclick,getActivity());
                                System.out.println("xxx");
                                if (Build.VERSION.SDK_INT >= 23)
                                {
                                    if (isReadStorageAllowed())
                                    {
                                        //If permission is already having then showing the toast
                                        //Existing the method with return
                                        System.out.println("You already have the permission");
                                        rv.setAdapter(rvAdapterAudioList);
                                        tab_rv.setAdapter(rvAdapterAudioList);

                                    } else
                                    //If the app has not the permission then asking for the permission
                                    {
                                        System.out.println("You dont already have the permission");
                                        requestStoragePermission();
                                        rv.setAdapter(rvAdapterAudioList);
                                        tab_rv.setAdapter(rvAdapterAudioList);
                                    }
                                }
                                rv.setAdapter(rvAdapterAudioList);
                                tab_rv.setAdapter(rvAdapterAudioList);
                                rvAdapterAudioList.notifyDataSetChanged();
                                System.out.println("adapter of tab_rv : "+tab_rv.getAdapter());
                                System.out.println("xxx1");
                                rv.setHasFixedSize(true);
                                tab_rv.setHasFixedSize(true);
                                progress.setVisibility(View.GONE);
                                rv.addOnItemTouchListener(
                                        new RecyclerItemClickListener(getActivity() ,new RecyclerItemClickListener.OnItemClickListener() {
                                            @Override public void onItemClick(View view, final int position)
                                            {
                                                synchronized (this)
                                                {

                                                }

                                            }


                                        })
                                );


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();



                    params.put("fid", "3");
                    params.put("category_id", "1");



                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//            int socketTimeout = 30000;//30 seconds - change to what you want
//            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);


        }



        expandableListAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        // setting list rvAdapterAudioList
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(final int groupPosition) {

                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition)
                {

                    expandableListView.collapseGroup(lastExpandedPosition);

                }
                lastExpandedPosition = groupPosition;



            }
        });

        // Listview Group collasped listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        // Listview on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        final int groupPosition, final int childPosition, long id) {
                // TODO Auto-generated method stub


                framelayout_forclick.setVisibility(View.GONE);
                expandableListView.setVisibility(View.INVISIBLE);


                tab_rv.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);

                cat_id=Integer.toString(groupPosition+1);
                sub_cat_id=Integer.toString(childPosition+1);
                System.out.println("***************************************************");
                System.out.println("groupPosition + "+groupPosition);
                System.out.println("childPosition + "+childPosition);
                System.out.println("cat_id + "+cat_id);
                System.out.println("sub_cat_id + "+sub_cat_id);
                System.out.println("***************************************************");
                if(groupPosition==0){
                    if(childPosition==0){
                        filter_id="1";
                    }
                    if(childPosition==1){
                        filter_id="2";
                    }
                    if(childPosition==2){
                        filter_id="3";
                    }
                    url3 = Constant.BASE_URL+"services/response.php?fid=12&cat_id=1&subcat_id=1&filter_id="+filter_id+"&filter_status=filter";
                }
                if(groupPosition==1){
                    if(childPosition==0){
                        filter_id="4";
                    }
                    if(childPosition==1){
                        filter_id="5";
                    }
                    if(childPosition==2){
                        filter_id="6";
                    }
                    url3 = Constant.BASE_URL+"services/response.php?fid=12&cat_id=1&subcat_id=1&filter_id="+filter_id+"&filter_status=filter";
                }
                if(groupPosition==2){
                    if(childPosition==0){
                        filter_id="7";
                    }
                    if(childPosition==1){
                        filter_id="8";
                    }
                    if(childPosition==2){
                        filter_id="9";
                    }
                    url3 = Constant.BASE_URL+"services/response.php?fid=12&cat_id=1&subcat_id=1&filter_id="+filter_id+"&filter_status=filter";
                }

                if(groupPosition==3 || groupPosition==4){
                    url3 = Constant.BASE_URL+"services/response.php?fid=12&cat_id=1&subcat_id=1&filter_id="+filter_id+"&filter_status=content";
                }



                json(groupPosition,childPosition);
                return false;

            }

        });



        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if (i == 1) {
                    //Single click
                    framelayout_forclick.setVisibility(View.VISIBLE);
                    expandableListView.setVisibility(View.VISIBLE);
                } else if (i == 2) {
                    //Double click
                    framelayout_forclick.setVisibility(View.GONE);
                    expandableListView.setVisibility(View.GONE);
                    i = 0;
                }
            }
        });




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

                minimizeplay.setVisibility(View.GONE);
                downplay.setVisibility(View.GONE);
                pauseeplay.setVisibility(View.VISIBLE);
                if(mediaPlayer.isPlaying())
                {
                    implay.setBackgroundResource(R.drawable.newplay);
                    mediaPlayer.pause();

                }
                else
                {

                    implay.setBackgroundResource(R.drawable.new_pause);
                    mediaPlayer.start();
                    Intent serviceIntent = new Intent(getActivity(), NotificationService.class);
                    serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    getActivity().startService(serviceIntent);
                    System.out.println("clickd1");
                }

            }
        });


        minimizeplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("panel state in fadeonclick : "+mLayout.getPanelState());
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                if(mediaPlayer.isPlaying()) {
                    minimizeplay.setVisibility(View.GONE);
                    downplay.setVisibility(View.GONE);
                    pauseeplay.setVisibility(View.VISIBLE);
                }else{
                    minimizeplay.setVisibility(View.GONE);
                    downplay.setVisibility(View.VISIBLE);
                    pauseeplay.setVisibility(View.GONE);
                }
            }
        });

        pauseeplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("inside pauseeplay click");
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    implay.setBackgroundResource(R.drawable.newplay);
                    pauseeplay.setVisibility(View.GONE);
                    minimizeplay.setVisibility(View.GONE);
                    downplay.setVisibility(View.VISIBLE);


                }
            }
        });

        implay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {        implay.setBackgroundResource(R.drawable.newplay);

                    mediaPlayer.pause();
                    minimizeplay.setVisibility(View.GONE);
                    downplay.setVisibility(View.VISIBLE);
                    pauseeplay.setVisibility(View.GONE);
                }
                else
                {   implay.setBackgroundResource(R.drawable.new_pause);
                    mediaPlayer.start();
                    minimizeplay.setVisibility(View.GONE);
                    downplay.setVisibility(View.GONE);
                    pauseeplay.setVisibility(View.VISIBLE);
                    Intent serviceIntent = new Intent(getActivity(), NotificationService.class);
                    serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    getActivity().startService(serviceIntent);
                }

            }
        });
        mImageView = (ImageView) v.findViewById(R.id.coverImage);
        rv.setHasFixedSize(true);
        TextView currentTime = (TextView) v.findViewById(R.id.currentTime);



        Context con=getActivity();

        final String coverImage ="https://dl.dropboxusercontent.com/u/2763264/RSS%20MP3%20Player/img3.jpg";



        String image_url = coverImage;


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(final MediaPlayer mp)
            {
                if(mp.isPlaying()) {
                    implay.setBackgroundResource(R.drawable.newplay);

                    mediaPlayer.pause();

                }
                else {
                    implay.setBackgroundResource(R.drawable.new_pause);
                    mediaPlayer.start();
                    System.out.println("clickd2");
                }

                mRunnable.run();

            }
        });


        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                implay.setBackgroundResource(R.drawable.newplay);
                seekBar.setProgress(0);

                downplay.setBackgroundResource(R.drawable.ic_action_play);
            }

        });


        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("", "onPanelSlide, offset " + panel);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i("", "onPanelStateChanged " + newState);
                if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    minimizeplay.setVisibility(View.GONE);
                    if(mediaPlayer.isPlaying()) {
                        downplay.setVisibility(View.GONE);
                        pauseeplay.setVisibility(View.VISIBLE);
                    }else {
                        downplay.setVisibility(View.VISIBLE);
                        pauseeplay.setVisibility(View.GONE);
                    }
                }
                if(newState == SlidingUpPanelLayout.PanelState.EXPANDED){

                    minimizeplay.setVisibility(View.VISIBLE);
                    if(mediaPlayer.isPlaying()) {
                        downplay.setVisibility(View.GONE);
                        pauseeplay.setVisibility(View.GONE);
                    }else{
                        downplay.setVisibility(View.GONE);
                        pauseeplay.setVisibility(View.GONE);
                    }
                }
            }
        });



        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener()
        {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                    if(newQuery.equals("")){

                        slide_layout.setVisibility(View.VISIBLE);

                    }

                mSearchView.showProgress();

                rv.scrollToPosition(0);

                mSearchView.hideProgress();
                if(rvAdapterAudioList !=null) {

                    rvAdapterAudioList.filter(newQuery);
                }
                if(rvAdapterFilterAudio !=null) {

                    rvAdapterFilterAudio.filter(newQuery);
                }

                if(rvAdapterFilterAudioContent !=null) {

                    rvAdapterFilterAudioContent.filter(newQuery);
                }
                slide_layout.setVisibility(View.GONE);
            }




            //  }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                slide_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFocusCleared() {
                slide_layout.setVisibility(View.VISIBLE);
            }
        });
        mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {
                slide_layout.setVisibility(View.VISIBLE);
            }
        });
        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                slide_layout.setVisibility(View.VISIBLE);

            }
        });

        setHasOptionsMenu(true);



        return v;


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

    @Override
    public void onClick(View view) {
        // switch()
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void stop(View view){

        mediaPlayer.seekTo(0);
        mediaPlayer.pause();

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


    @Override
    public void onDestroy() {

        super.onDestroy();
        Intent serviceIntent = new Intent(getActivity(), NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        getActivity().startService(serviceIntent);
    }
    public boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == 1){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                System.out.println("Permission granted now you can read the storage");
                //Displaying a toast
                Toast.makeText(getActivity(),"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                System.out.println("Oops you just denied the permission");
                Toast.makeText(getActivity(),"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }


    public  void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Speeches");
        listDataHeader.add("Quthuba");
        listDataHeader.add("Voice Of Islam");
        listDataHeader.add("Islamic Songs");
        listDataHeader.add("Quran Recitation");

        List<String> Speeches = new ArrayList<String>();
        Speeches.add("Orator");
        Speeches.add("Subject");
        Speeches.add("Programme");



        List<String> Quthuba = new ArrayList<String>();
        Quthuba.add("New");
        Quthuba.add("Katheeb");
        Quthuba.add("Masjid");

        List<String> VoiceOfslam = new ArrayList<String>();
        VoiceOfslam.add("Thoughts");
        VoiceOfslam.add("Other Updates");
        VoiceOfslam.add("Promotional");
        List<String> ISLAMIC_SONGS = new ArrayList<String>();
        ISLAMIC_SONGS.add("Malayalam");
        ISLAMIC_SONGS.add("English");
        ISLAMIC_SONGS.add("Arabic");
        List<String> QURAN_RECIATIONS= new ArrayList<String>();
        QURAN_RECIATIONS.add("Quran Recitation");
        QURAN_RECIATIONS.add("Quran Translation");




        listDataChild.put(listDataHeader.get(0),Speeches); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Quthuba);
        listDataChild.put(listDataHeader.get(2), VoiceOfslam);
        listDataChild.put(listDataHeader.get(3), ISLAMIC_SONGS);
        listDataChild.put(listDataHeader.get(4), QURAN_RECIATIONS);

    }
    private Intent getShareIntent(String type, String subject, String text)
    {
        boolean found = false;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getActivity().getPackageManager().queryIntentActivities(share, 0);
        System.out.println("resinfo: " + resInfo);
        if (!resInfo.isEmpty()){
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type) ) {
                    share.putExtra(Intent.EXTRA_SUBJECT,  subject);
                    share.putExtra(Intent.EXTRA_TEXT,     text);
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found)
                return null;

            return share;
        }
        return null;
    }

    private void json(final  int grouppos,final  int childpos) {
        int  pos=childpos+1;
        int cat_id=grouppos+1;
        int  sub_id=childpos+1;
        System.out.println("++++++++++++++categoryiddddddddddddddd :" +cat_id+"subcategoryidddddddddddddd"+sub_id+"possssss"+pos );

        RequestQueue queue3 = Volley.newRequestQueue(getActivity());
        progress.setVisibility(View.VISIBLE);
        System.out.println("url3 : "+url3);
        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                System.out.println("++++++++++++++RESPONSE+++++++++++++++ gallery :" + response);
                if (response != null && !response.isEmpty()) {

                    JSONArray jsonarray;
                    JSONObject jsonobject;
                    //  LibraryModel lb;
                    try {
                        arrayList=new ArrayList<>();
                        jsonarray = new JSONArray(response);
                        System.out.println("g2");
                        if(jsonarray.length()==0){
                            rv.setVisibility(View.GONE);
                            progress.setVisibility(View.GONE);
                        }else {
                            rv.setVisibility(View.VISIBLE);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                AudioFilterModel audioFilterModel = new AudioFilterModel();
                                AudioModel audioModel = new AudioModel();

                                System.out.println("g3");
                                jsonobject = jsonarray.getJSONObject(i);
                                String filter_content_id = jsonobject.getString("filter_content_id");
                                String filter_content = jsonobject.getString("filter_content");

                                audioFilterModel.setFilter_content_id(filter_content_id);
                                audioFilterModel.setFilter_content(filter_content);
                                audioModel.setFilter_content(filter_content);

                                arrayList.add(audioFilterModel);
                                audioModelArrayList.add(audioModel);

                            }
                        }


                        rvAdapterFilterAudio = new RVAdapterFilterAudio(arrayList,getActivity());
                        System.out.println("xxx");
                        progress.setVisibility(View.GONE);
                        rv.setAdapter(rvAdapterFilterAudio);
                        rvAdapterFilterAudio.notifyDataSetChanged();
                        System.out.println("xxx1");
                        rv.addOnItemTouchListener(
                                new RecyclerItemClickListener(getActivity() ,new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override public void onItemClick(View view, final int position) {
                                        synchronized (this) {
                                            framelayout_forclick.setVisibility(View.GONE);
                                            expandableListView.setVisibility(View.INVISIBLE);
                                            final String s=arrayList.get(position).getFilter_content_id();
                                            System.out.println("String...............++++++++++" + s);

                                            NetworkCheckingClass networkCheckingClass1 = new NetworkCheckingClass(getActivity());
                                            boolean is = networkCheckingClass1.ckeckinternet();
                                            if (is) {

                                                String   REGISTER_URLs=Constant.BASE_URL+"services/response.php";
                                                StringRequest stringRequest0 = new StringRequest(Request.Method.POST, REGISTER_URLs,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                subCategoryModelArrayList2=new ArrayList<>();
                                                                AudioFilterContentModel audioFilterContentModel;
                                                                String title=null;

                                                                try {
                                                                    System.out.println("response in filter : "+response);
                                                                    JSONArray jsonArray=new JSONArray(response);
                                                                    for(int i=0;i< jsonArray.length();i++)
                                                                    {

                                                                        JSONObject jsonobject =jsonArray.getJSONObject(i);
                                                                        audioFilterContentModel =new AudioFilterContentModel();

                                                                        String category_id = jsonobject.getString("category_id");
                                                                        String sub_category_id = jsonobject.getString("sub_category_id");
                                                                        String   audio = jsonobject.getString("audio");
                                                                        String    thumbnail = jsonobject.getString("thumbnail");
                                                                        String id = jsonobject.getString("id");
                                                                        if(jsonobject.has("title")) {
                                                                            title = jsonobject.getString("title");
                                                                        }
                                                                        else {
                                                                            title=null;
                                                                        }
                                                                        //  String Programme = jsonobject.getString("Programme");
                                                                        audioFilterContentModel.setCategory_id(category_id);
                                                                        System.out.println("xxxid" + id+category_id+sub_category_id+audio+thumbnail+title);
                                                                        audioFilterContentModel.setSub_category_id(sub_category_id);
                                                                        audioFilterContentModel.setAudio(audio);

                                                                        audioFilterContentModel.setTitle(title);

                                                                        audioFilterContentModel.setThumbnail(thumbnail);

                                                                        subCategoryModelArrayList2.add(audioFilterContentModel);


                                                                    }
                                                                    progress.setVisibility(View.GONE);
                                                                    rvAdapterFilterAudioContent =new RVAdapterFilterAudioContent(subCategoryModelArrayList2,getActivity());
                                                                    System.out.println("xxxsss");
                                                                    if (Build.VERSION.SDK_INT >= 23) {
                                                                        if (isReadStorageAllowed()) {
                                                                            //If permission is already having then showing the toast
                                                                            //Existing the method with return
                                                                            System.out.println("You already have the permission");
                                                                            //  listView.setAdapter(rvAdapterAudioList);
                                                                            rv.setAdapter(rvAdapterFilterAudioContent);
                                                                            rvAdapterFilterAudioContent.notifyDataSetChanged();

                                                                        } else
                                                                        //If the app has not the permission then asking for the permission
                                                                        {
                                                                            System.out.println("You dont already have the permission");
                                                                            requestStoragePermission();
                                                                            rv.setAdapter(rvAdapterFilterAudioContent);
                                                                            rvAdapterFilterAudioContent.notifyDataSetChanged();
                                                                        }
                                                                    }

                                                                    System.out.println("xxx1");
                                                                    rv.addOnItemTouchListener(
                                                                            new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                                                                @Override
                                                                                public void onItemClick(View view, final int position) {
                                                                                    synchronized (this) {
                                                                                        framelayout_forclick.setVisibility(View.GONE);
                                                                                        expandableListView.setVisibility(View.INVISIBLE);


                                                                                    }

                                                                                }


                                                                            })
                                                                    );


                                                                }
                                                                catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {

                                                            }
                                                        })

                                                {
                                                    @Override
                                                    protected Map<String, String> getParams() throws AuthFailureError {
                                                        Map<String, String> params = new HashMap<String, String>();


                                                        params.put("fid","13");
                                                        params.put("filter_content_id", s);


                                                        return params;
                                                    }

                                                };

                                                RequestQueue requestQueue0 = Volley.newRequestQueue(getActivity());
//            int socketTimeout = 30000;//30 seconds - change to what you want
//            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//            stringRequest.setRetryPolicy(policy);
                                                requestQueue0.add(stringRequest0);


                                            }



                                        }

                                    }


                                })
                        );

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progress.setVisibility(View.GONE);
                    rv.setVisibility(View.GONE);
                    System.out.println("nothing to displayy");
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue3.add(stringRequest3);
    }
    public void switchRecyclerView(){
        rv.setAdapter(tab_rv.getAdapter());
        rv.setVisibility(View.VISIBLE);

    }

    class swipeLoadData extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swiperefreshlayout.setRefreshing(true);
            framelayout_forclick.setVisibility(View.GONE);
            expandableListView.setVisibility(View.INVISIBLE);
            rv.setVisibility(View.VISIBLE);
            tab_rv.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(REGISTER_URL);

                JSONObject postDataParams=new JSONObject();
                postDataParams.put("fid", "3");
                postDataParams.put("category_id", "1");

                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("responseeeee1"+s);
            framelayout_forclick.setVisibility(View.GONE);

            audioModelArrayList =new ArrayList<>();
            AudioModel audioModel;

            try {

                JSONArray jsonArray=new JSONArray(s);
                for(int i=0;i< jsonArray.length();i++)
                {

                    JSONObject jsonobject =jsonArray.getJSONObject(i);
                    audioModel =new AudioModel();
                    String category_id = jsonobject.getString("category_id");
                    String sub_category_id = jsonobject.getString("sub_category_id");
                    audio = jsonobject.getString("audio");
                    thumbnail = jsonobject.getString("thumbnail");
                    String Orator = jsonobject.getString("orator_name");
                    String  title = jsonobject.getString("title");
                    String Subject = jsonobject.getString("Subject");
                    String Programme = jsonobject.getString("programme_name");
                    String NEW = jsonobject.getString("new");
                    audioModel.setCategory_id(category_id);
                    System.out.println("xxx"+audio);
                    audioModel.setSub_category_id(sub_category_id);
                    audioModel.setAudio(audio);
                    audioModel.setOrator(Orator);
                    audioModel.setTitle(title);
                    audioModel.setNEWW(NEW);
                    audioModel.setProgramme(Programme);
                    audioModel.setSubject(Subject);
                    audioModel.setThumbnail(thumbnail);
                    audioModelArrayList.add(audioModel);



                }

                filteredlistclick= audioModelArrayList;
                rvAdapterAudioList = new RVAdapterAudioList(filteredlistclick,getActivity());
                System.out.println("xxx");

                rv.setAdapter(rvAdapterAudioList);
                tab_rv.setAdapter(rv.getAdapter());


                rvAdapterAudioList.notifyDataSetChanged();
                System.out.println("xxx1");
                rv.setHasFixedSize(true);
                rv.addOnItemTouchListener(
                        new RecyclerItemClickListener(getActivity() ,new RecyclerItemClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, final int position)
                            {
                                synchronized (this)
                                {


                                }

                            }


                        })
                );

                swiperefreshlayout.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
    private void func_banner() {

        RequestQueue queue1 = Volley.newRequestQueue(getActivity());
        String url1 = Constant.BASE_URL+"services/response.php?fid=22";

        StringRequest stringRequest1 = new StringRequest
                (Request.Method.GET, url1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                                            System.out.println("++++++++++++++RESPONSE+++++++++++++++banner    :" + response);



                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++)
                            { DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String banner_id = jsonobject.getString("id");
                                String banner = jsonobject.getString("banner");
                                defaultSliderView.image(banner);
                                sliderShow.addSlider(defaultSliderView);
                            }






                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue1.add(stringRequest1);
        /*sliderShow.setPresetTransformer(SliderLayout.Transformer.Accordion);*/
        sliderShow.setPresetTransformer(SliderLayout.Transformer.Stack);
        sliderShow.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);




    }


}
