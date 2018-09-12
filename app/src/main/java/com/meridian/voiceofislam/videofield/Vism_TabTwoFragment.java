package com.meridian.voiceofislam.videofield;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.meridian.voiceofislam.Constant;
import com.meridian.voiceofislam.NetworkCheckingClass;
import com.meridian.voiceofislam.R;
import com.tuyenmonkey.mkloader.MKLoader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by iFocus on 27-10-2015.
 */
public class Vism_TabTwoFragment extends Fragment implements SearchView.OnQueryTextListener {

    static MKLoader progress;
    String result;
    List<String> s=new ArrayList<>();
    String Title_of_rg;

    int i=0;
    static RecyclerView recyclerview,tab_rv2;
    List<CountryModel1> mCountryModel1;
    List<CountryModel2> mCountryModel2;
    RVAdapter1 adapter1;
    CountryModel1 cm;
    String id,cat_id,sub_cat_id,video,thumnail,title;
    private FloatingSearchView mSearchView;

    private   String REGISTER_URL = Constant.BASE_URL+"services/response.php";

    String cat_id3,sub_cat_id3,video3,thumbnail3,title3;
    List<VideoCatModel> catmod;
    VideoCatModel v_ctm;
    JSONArray mArray;
    JSONObject mJsonObject;
    TextView tit;
    RecyclerAdapterS adaptersp;
    ImageView im;

    FrameLayout framelayout_forclick;

    ImageView options;
    String k;
    private int lastExpandedPosition = -1;




    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    String filter_id,passing_cat_id;
    static Context sending_context;
    static SwipeRefreshLayout swiperefreshlayout;
    int swiperefreshflag=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.vism_tab_two_fragment, container, false);

        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        options= (ImageView) view.findViewById(R.id.img_menu);
        framelayout_forclick=(FrameLayout)view.findViewById(R.id.framelayout_forclick);
        framelayout_forclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableListView.setVisibility(View.GONE);
                framelayout_forclick.setVisibility(View.GONE);
            }
        });
        ExpandableListDataPump();;
        swiperefreshlayout=(SwipeRefreshLayout)view.findViewById(R.id.swiperefreshlayout);
        swiperefreshlayout.setColorSchemeResources(
                R.color.colorPrimary
        );
        progress = (MKLoader) view.findViewById(R.id.progress_bar);
        tab_rv2=(RecyclerView)view.findViewById(R.id.tab_rv2);
        GridLayoutManager layoutManagerrr = new GridLayoutManager(getActivity(), 2);


        layoutManagerrr.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
        tab_rv2.setLayoutManager(layoutManagerrr);

        if (DetectConnection
                .checkInternetConnection(getActivity())) {

            swiperefreshflag=0;
            new DownloadData().execute();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Sorry");
            alertDialog.setMessage("No network");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            dialog.dismiss();

                        }
                    });
            alertDialog.show();

        }

        // Create a grid layout with two columns
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);


        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);



        im=(ImageView) view.findViewById(R.id.imageView7);
        tit = (TextView) view.findViewById(R.id.textView3);



        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NetworkCheckingClass networkCheckingClass1 = new NetworkCheckingClass(getActivity());
                boolean is = networkCheckingClass1.ckeckinternet();
                if (is)
                {
                    swiperefreshflag=1;
                    new DownloadData().execute();
                }


            }
        });

        expandableListView = (ExpandableListView)view. findViewById(R.id.expandableListView);

        expandableListAdapter = new CustomExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition)
                {

                    expandableListView.collapseGroup(lastExpandedPosition);

                }
                lastExpandedPosition = groupPosition;

            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                framelayout_forclick.setVisibility(View.GONE);
                expandableListView.setVisibility(View.GONE);
                //vcm.

                tab_rv2.setVisibility(View.GONE);
                recyclerview.setVisibility(View.VISIBLE);



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


                    passing_cat_id="1";
                    k="1";

                }
                if(groupPosition==1){
                    if(childPosition==0){
                        filter_id="1";
                    }   if(childPosition==1){
                        filter_id="2";
                    }

                    passing_cat_id="2";
                    k="2";
                }
                if(groupPosition==2){
                    if(childPosition==0){
                        filter_id="1";
                    }
                    if(childPosition==1){
                        filter_id="2";
                    }
                    if(childPosition==2){
                        filter_id="3";
                    }
                    passing_cat_id="3";
                    k="3";
                }





                System.out.println("<<%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%>>>>" +k);
                registerUser3(passing_cat_id,filter_id);
                mSearchView.setVisibility(View.VISIBLE);
                return false;
            }
        });




        recyclerview.setLayoutManager(layoutManager);
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                try {
                    final List<CountryModel1> filteredModelList = filter(mCountryModel1, newQuery);
                    mSearchView.setVisibility(View.VISIBLE);
                    adapter1.setFilter(filteredModelList);
                }catch (Exception e){
                    e.printStackTrace();
                }
                //  return true

            }
        });
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   registerUser1();
                swiperefreshflag=0;
                new DownloadData().execute();
                mSearchView.setVisibility(View.INVISIBLE);
                im.setVisibility(View.GONE);
                tit.setVisibility(View.GONE);
            }
        });

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if (i == 1) {
                    //Single click
                    expandableListView.setVisibility(View.VISIBLE);
                    framelayout_forclick.setVisibility(View.VISIBLE);
                } else if (i == 2) {
                    //Double click
                    framelayout_forclick.setVisibility(View.GONE);
                    expandableListView.setVisibility(View.GONE);
                    i = 0;
                    //   Toast.makeText(getActivity(),"clicked",Toast.LENGTH_LONG).show();
                }
            }
        });


        return  view;
    }




    private void registerUser3(final String passing_cat,final String filter_id) {
        progress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("video response : " + response);
                        if (response != null && !response.isEmpty()){

                            catmod = new ArrayList<VideoCatModel>();
                            mCountryModel2 = new ArrayList<>();
                            mCountryModel1 = new ArrayList<>();
                            mCountryModel1.clear();
                            try {
                                mArray = new JSONArray(response);

                                if (mArray.length() == 0) {
                                    progress.setVisibility(View.GONE);
                                    recyclerview.setVisibility(View.GONE);

                                } else {
                                    recyclerview.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < mArray.length(); i++) {
                                        v_ctm = new VideoCatModel();
                                        cm = new CountryModel1();

                                        mJsonObject = mArray.getJSONObject(i);
                                        Log.d("OutPut", mJsonObject.getString("category_id"));
                                        Log.d("OutPut2", mJsonObject.getString("sub_category_id"));
                                        Log.d("OutPut3", mJsonObject.getString("video"));
                                        Log.d("OutPut2", mJsonObject.getString("thumbnail"));


                                        String person_name = mJsonObject.getString("person_name");
                                        String subject = mJsonObject.getString("subject");
                                        sub_cat_id3 = mJsonObject.getString("sub_category_id");
                                        video3 = mJsonObject.getString("video");
                                        thumbnail3 = mJsonObject.getString("thumbnail");
                                        String Video_thumbail=  "http://img.youtube.com/vi/"+video3+"/hqdefault.jpg";
                                        v_ctm.setCat_id(cat_id3);
                                        v_ctm.setSub_cat_id(sub_cat_id3);
                                        v_ctm.setVideo(video3);
                                        v_ctm.setPersonname(person_name);
                                        v_ctm.setSubject(subject);
                                        v_ctm.setThumbnail(Video_thumbail);
                                        catmod.add(v_ctm);

                                        System.out.println("" + catmod);


                                        System.out.println("<<<33333333333333cat_id>>>>" + cat_id3);
                                        System.out.println("<<<3333333333333sub_cat_id>>>" + sub_cat_id3);
                                        System.out.println("<<<3333333333333video>>>" + video3);
                                        System.out.println("<<<333333333333thumbnail>>>>" + thumbnail3);
                                        System.out.println("<<<33333333333333333title>>>>" + title3);
                                        recyclerview.setHasFixedSize(true);
                                        //to use RecycleView, you need a layout manager. default is LinearLayoutManager
                                        //    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                                        // linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);


                                        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

                                        // Create a custom SpanSizeLookup where the first item spans both columns
                                        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                            @Override
                                            public int getSpanSize(int position) {
                                                return position == 0 ? 2 : 1;
                                            }
                                        });

                                        recyclerview.setLayoutManager(layoutManager);

                                        adaptersp = new RecyclerAdapterS(catmod, getActivity());
                                        recyclerview.setAdapter(adaptersp);
                                        mCountryModel1.add(cm);
                                        mSearchView.setVisibility(View.VISIBLE);

                                        progress.setVisibility(View.GONE);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {

                            MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(getActivity())
                                    .setTitle("Empty!")
                                    .setDescription("Currently no data is available")
                                    .setHeaderColor(R.color.colorPrimaryDark)
                                    .setPositiveText("OK")

                                    .setStyle(Style.HEADER_WITH_TITLE)
// .withDialogAnimation(true, Duration.FAST)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                            progress.setVisibility(View.GONE);
                                        }
                                    })

                                    .build();


                            dialog.show();


                            System.out.println("nothing to displayy");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("category_id","2");
                params.put("subcategory_id",passing_cat);
                params.put("filter_status","filter");
                params.put("filter_id",filter_id);
                params.put("fid","18");
                System.out.println("#############################################");


                System.out.println("subcategory_id : "+passing_cat_id);
                System.out.println("filter_status : "+"filter");
                System.out.println("filter_id : "+filter_id);
                System.out.println("#############################################");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }








    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);



    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setVisibility(View.GONE);

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<CountryModel1> filteredModelList = filter(mCountryModel1, newText);
        adapter1.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    private List<CountryModel1> filter(List<CountryModel1> models, String query) {
        query = query.toLowerCase();

        final List<CountryModel1> filteredModelList = new ArrayList<>();
        for (CountryModel1 model1 : models) {
            final String text = model1.getSubject().toLowerCase();
            final String text1 = model1.getSubject().toUpperCase();
            final String text2 = model1.getPersonname().toLowerCase();
            final String text3 = model1.getPersonname().toUpperCase();
            if (text.contains(query)||text1.contains(query)||text2.contains(query)||text3.contains(query)) {
                filteredModelList.add(model1);
            }
        }
        return filteredModelList;
    }






    class DownloadData extends AsyncTask<Void, Void, Void> {

        ProgressDialog pd = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            if(swiperefreshflag==1){
                swiperefreshlayout.setRefreshing(true);
            }else{
                progress.setVisibility(ProgressBar.VISIBLE);
            }



        }

        @Override
        protected Void doInBackground(Void... params) {

            try {



                HttpClient httpclient = new DefaultHttpClient();


                HttpPost httppost = new HttpPost(Constant.BASE_URL+"services/response.php?fid=3&category_id=2"
                );
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();


            } catch (Exception e) {
                Log.e("Loading connection  :", e.toString());
            }


            return null;
        }

        protected void onPostExecute(Void args) {
            try {
            progress.setVisibility(ProgressBar.GONE);
            String sam = result.trim();
            System.out.println(">>>>>>>>>>>>>>>" + result);
            System.out.println(">>>>>>>>>>>>>>>" + sam);
            String value = result;
            if
                    (result.equalsIgnoreCase("[]")) {
                Toast.makeText(getActivity(), "No Events", Toast.LENGTH_SHORT).show();
            } else {
                JSONArray mArray;
                mCountryModel1 = new ArrayList<>();
                mCountryModel1.clear();

                    mArray = new JSONArray(result);
                    for (int i = 0; i < mArray.length(); i++) {

                        cm=new CountryModel1();


                        JSONObject mJsonObject = mArray.getJSONObject(i);
                        Log.d("OutPut", mJsonObject.getString("id"));
                        Log.d("OutPut2", mJsonObject.getString("category_id"));
                        Log.d("OutPut3", mJsonObject.getString("sub_category_id"));

                        Log.d("OutPut2", mJsonObject.getString("video"));
                        Log.d("OutPut3", mJsonObject.getString("thumbnail"));

                        Log.d("OutPut4", mJsonObject.getString("title"));
                        String person_name;
                        if(mJsonObject.getString("person_name").contentEquals("")||mJsonObject.getString("person_name").contentEquals("null")){
                            person_name =" " ;
                        }
                        else {
                            person_name =mJsonObject.getString("person_name") ;
                        }




                        String subject;
                        if(mJsonObject.getString("subject").contentEquals("")||mJsonObject.getString("subject").contentEquals("null")){
                             subject =" " ;
                        }
                        else {
                             subject =mJsonObject.getString("subject") ;
                        }

                        id = mJsonObject.getString("id");
                        cat_id= mJsonObject.getString("category_id");
                        sub_cat_id= mJsonObject.getString("sub_category_id");
                        video= mJsonObject.getString("video");
                        thumnail= mJsonObject.getString("thumbnail");
                        title= mJsonObject.getString("title");
                      String Video_thumbail=  "http://img.youtube.com/vi/"+video+"/hqdefault.jpg";
                        cm.setId(id);
                        cm.setCat_id(cat_id);
                        cm.setSub_cat_id(sub_cat_id);
                        cm.setVideo(video);
                        cm.setThumnail(Video_thumbail);
                        cm.setTitle(title);
                        cm.setPersonname(person_name);
                        cm.setSubject(subject);

                        System.out.println("<<<kk>>>>" + Title_of_rg);
//

                        mCountryModel1.add(cm);

                        System.out.println(""+mCountryModel1);
                        sending_context=getActivity();
                        adapter1 = new RVAdapter1(mCountryModel1,sending_context);
                        recyclerview.setAdapter(adapter1);
                        tab_rv2.setAdapter(adapter1);




                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            swiperefreshflag=0;
            swiperefreshlayout.setRefreshing(false);
        }

    }
    public void ExpandableListDataPump() {

        expandableListTitle = new ArrayList<String>();


        expandableListTitle.add("Islamic Videos");
        expandableListTitle.add("Documentaries");
        expandableListTitle.add("Thoughts");



        expandableListDetail = new HashMap<String, List<String>>();

        List<String> islamic_videos = new ArrayList<String>();
        islamic_videos.add(" Talks, seminars and discussions");
        islamic_videos.add("Animated videos for children");
        islamic_videos.add("Others");



        List<String> documentaries = new ArrayList<String>();
        documentaries.add("Islamic Documentaries");
        documentaries.add("Others");

        List<String> thoughts = new ArrayList<String>();
        thoughts.add("Thoughts");
        thoughts.add("Other Updates");
        thoughts.add("Promotional");
        expandableListDetail.put(expandableListTitle.get(0), islamic_videos);
        expandableListDetail.put(expandableListTitle.get(1), documentaries);
        expandableListDetail.put(expandableListTitle.get(2), thoughts);





    }
    public void switchRecyclerView(){
        recyclerview.setAdapter(tab_rv2.getAdapter());
        recyclerview.setVisibility(View.VISIBLE);
    }


}