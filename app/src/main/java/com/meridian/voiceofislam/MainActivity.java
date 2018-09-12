package com.meridian.voiceofislam;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.meridian.voiceofislam.Radio.AudioVisualizationFragment;
import com.meridian.voiceofislam.Radio.BlankFragment;
import com.meridian.voiceofislam.audioplayer.AudioFragment;
import com.meridian.voiceofislam.livestream.LiveStreamFragment;
import com.meridian.voiceofislam.sidebar.AboutUs;
import com.meridian.voiceofislam.sidebar.Contact;
import com.meridian.voiceofislam.sidebar.ListMp3;
import com.meridian.voiceofislam.videoaudioupload.Loginfragment;
import com.meridian.voiceofislam.videofield.Vism_TabTwoFragment;
import com.tuyenmonkey.mkloader.MKLoader;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AudioFragment.OnFragmentInteractionListener,LiveStreamFragment.OnFragmentInteractionListener,BlankFragment.OnFragmentInteractionListener {

    private static final int REQUEST_CODE = 1;
    static int tabno;
    ViewPager viewPager;
    TabLayout tabLayout;
    String  REGISTER_URL  =Constant.BASE_URL+"services/response.php";
    LinearLayout linear_audio,linear_vidio,linear_liveaudio,linear_livevidio,bottom_audio,bottom_video,bottom_liveaudio,bottom_livevideo;
    TextView txt_audio,txt_video,txt_liveaudio,txt_livevideo;
    int pos_flag;
    MKLoader progress_bar;
    LinearLayout loading_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(savedInstanceState==null)

        {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                requestPermissions();

            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        loading_layout=(LinearLayout)findViewById(R.id.loading_layout);

        progress_bar=(MKLoader) findViewById(R.id.progress_bar);
        linear_audio=(LinearLayout)findViewById(R.id.linear_audio);
        linear_vidio=(LinearLayout)findViewById(R.id.linear_vidio);
        linear_liveaudio=(LinearLayout)findViewById(R.id.linear_liveaudio);
        linear_livevidio=(LinearLayout)findViewById(R.id.linear_livevidio);
        bottom_audio=(LinearLayout)findViewById(R.id.bottom_audio);
        bottom_video=(LinearLayout)findViewById(R.id.bottom_video);
        bottom_liveaudio=(LinearLayout)findViewById(R.id.bottom_liveaudio);
        bottom_livevideo=(LinearLayout)findViewById(R.id.bottom_livevideo);

        txt_audio=(TextView)findViewById(R.id.txt_audio);
        txt_video=(TextView)findViewById(R.id.txt_video);
        txt_liveaudio=(TextView)findViewById(R.id.txt_liveaudio);
        txt_livevideo=(TextView)findViewById(R.id.txt_livevideo);

        txt_audio.setTypeface(null, Typeface.BOLD);
        txt_video.setTypeface(null, Typeface.BOLD);
        txt_liveaudio.setTypeface(null, Typeface.BOLD);
        txt_livevideo.setTypeface(null, Typeface.BOLD);


        viewPager=(ViewPager)findViewById(R.id.viewpager);
        tabLayout=(TabLayout)findViewById(R.id.tabs);
        viewPager.setOffscreenPageLimit(4);

        bottom_audio.setVisibility(View.VISIBLE);
        txt_audio.setTextColor(Color.parseColor("#ffffff"));
        linear_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos_flag=0;

                viewPager.setCurrentItem(0);
                bottom_audio.setVisibility(View.VISIBLE);
                bottom_video.setVisibility(View.INVISIBLE);
                bottom_liveaudio.setVisibility(View.INVISIBLE);
                bottom_livevideo.setVisibility(View.INVISIBLE);

                txt_audio.setTextColor(Color.parseColor("#ffffff"));
                txt_video.setTextColor(Color.parseColor("#787878"));
                txt_liveaudio.setTextColor(Color.parseColor("#787878"));
                txt_livevideo.setTextColor(Color.parseColor("#787878"));


                AudioFragment af=new AudioFragment();
                af.switchRecyclerView();
            }
        });
        linear_vidio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos_flag=1;
                viewPager.setCurrentItem(1);
                bottom_audio.setVisibility(View.INVISIBLE);
                bottom_video.setVisibility(View.VISIBLE);
                bottom_liveaudio.setVisibility(View.INVISIBLE);
                bottom_livevideo.setVisibility(View.INVISIBLE);

                txt_audio.setTextColor(Color.parseColor("#787878"));
                txt_video.setTextColor(Color.parseColor("#ffffff"));
                txt_liveaudio.setTextColor(Color.parseColor("#787878"));
                txt_livevideo.setTextColor(Color.parseColor("#787878"));

                Vism_TabTwoFragment vf=new Vism_TabTwoFragment();
                vf.switchRecyclerView();
            }
        });
        linear_liveaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos_flag=2;
                viewPager.setCurrentItem(2);
                bottom_audio.setVisibility(View.INVISIBLE);
                bottom_video.setVisibility(View.INVISIBLE);
                bottom_liveaudio.setVisibility(View.VISIBLE);
                bottom_livevideo.setVisibility(View.INVISIBLE);

                txt_audio.setTextColor(Color.parseColor("#787878"));
                txt_video.setTextColor(Color.parseColor("#787878"));
                txt_liveaudio.setTextColor(Color.parseColor("#ffffff"));
                txt_livevideo.setTextColor(Color.parseColor("#787878"));
            }
        });
        linear_livevidio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos_flag=3;
                viewPager.setCurrentItem(3);
                bottom_audio.setVisibility(View.INVISIBLE);
                bottom_video.setVisibility(View.INVISIBLE);
                bottom_liveaudio.setVisibility(View.INVISIBLE);
                bottom_livevideo.setVisibility(View.VISIBLE);

                txt_audio.setTextColor(Color.parseColor("#787878"));
                txt_video.setTextColor(Color.parseColor("#787878"));
                txt_liveaudio.setTextColor(Color.parseColor("#787878"));
                txt_livevideo.setTextColor(Color.parseColor("#ffffff"));
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
        new LoadViewPager().execute();
            }
        }, 1000);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:

                        linear_audio.performClick();


                        break;
                    case  1:

                        bottom_audio.setVisibility(View.INVISIBLE);
                        bottom_video.setVisibility(View.VISIBLE);
                        bottom_liveaudio.setVisibility(View.INVISIBLE);
                        bottom_livevideo.setVisibility(View.INVISIBLE);

                        txt_audio.setTextColor(Color.parseColor("#787878"));
                        txt_video.setTextColor(Color.parseColor("#ffffff"));
                        txt_liveaudio.setTextColor(Color.parseColor("#787878"));
                        txt_livevideo.setTextColor(Color.parseColor("#787878"));

                        break;
                    case 2:

                        linear_liveaudio.performClick();

                        break;
                    case 3:

                        linear_livevidio.performClick();

                        break;
                    default:


                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        if (id == R.id.menuSortOrator) {

            Toast.makeText(getApplicationContext(),"Sorting by Orator",Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.menuSortSubject) {
            Toast.makeText(getApplicationContext(),"Sorting by Subject",Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.menuSortProgram) {
            Toast.makeText(getApplicationContext(),"Sorting by Program",Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            Intent s=new Intent(getApplicationContext(),AboutUs.class);
            startActivity(s);


        } else if (id == R.id.nav_upload) {
            Intent inte=new Intent(getApplicationContext(), Loginfragment.class);
            startActivity(inte);

        } else if (id == R.id.nav_download) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                Intent s = new Intent(getApplicationContext(), ListMp3.class);
                startActivity(s);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Feature Not Available Due To Permission Denial",Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_contact) {
            Intent s=new Intent(getApplicationContext(),Contact.class);
            startActivity(s);
        }
        else if (id == R.id.nav_feedback) {
            Intent s=new Intent(getApplicationContext(),Feedback.class);
            startActivity(s);
        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }


    private void requestPermissions()
    {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE
        );
    }

    private void permissionsNotGranted()
    {
        Toast.makeText(this, R.string.toast_permissions_not_granted, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.


                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    @Override
    public void onBackPressed() {

        AudioFragment af=new AudioFragment();

            MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(this)
                    .setTitle("ALERT!")
                    .setDescription("Do You Want to Exit??")
                    .setHeaderColor(R.color.colorPrimaryDark)
                    .setPositiveText("OK")
                    .setNegativeText("CANCEL")
                    .setStyle(Style.HEADER_WITH_TITLE)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            MainActivity.super.onBackPressed();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback(){
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();

                        }
                    })
                    .build();


            dialog.show();




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

public class LoadViewPager extends AsyncTask<String,String,String>{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading_layout.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... strings) {


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                tabno = position;


                switch (position){

                    case 0:
                    {

                        AudioFragment audioFragment=new AudioFragment();


                        return  audioFragment;
                    }

                    case 1:

                        Vism_TabTwoFragment vnf=new Vism_TabTwoFragment();
                        return vnf;

                    case 2:

                        AudioVisualizationFragment blnk = new AudioVisualizationFragment();
                        return blnk;

                    case 3:LiveStreamFragment lsf=new LiveStreamFragment();
                        return lsf;



                    default: BlankFragment tabf1 = new BlankFragment();
                        return tabf1;



                }


            }

            @Override
            public CharSequence getPageTitle(int position)
            {

                if(position==0) {

                    return "Audio";
                }

                if(position==1) {

                    return "Video";
                }



                if(position==2) {

                    return "Live Audio";
                }

                if(position==3) {

                    return "Live Video";
                }


                else {
                    return  null;

                }

            }

            @Override
            public int getCount() {
                return 4;
            }
        });


        tabLayout.setupWithViewPager(viewPager);
        loading_layout.setVisibility(View.GONE);
    }
}
}
