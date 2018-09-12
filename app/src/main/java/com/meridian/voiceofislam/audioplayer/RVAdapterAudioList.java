package com.meridian.voiceofislam.audioplayer;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meridian.voiceofislam.Constant;
import com.meridian.voiceofislam.R;
import com.meridian.voiceofislam.database.DatabaseHelper;
import com.meridian.voiceofislam.database.TableModel;
import com.meridian.voiceofislam.permission.CheckAndRequestPermission;
import com.meridian.voiceofislam.sidebar.ListMp3;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by user1 on 14-10-2015.
 */
public class RVAdapterAudioList extends RecyclerView.Adapter<RVAdapterAudioList.PersonViewHolder>  {
    RelativeLayout play, share;
    TextView cancel;
    Context context;
    List<AudioModel> itemsCopy = new ArrayList<>();
    private List<AudioModel> audioModelArrayList;
    View v;
    String audio_url;

    static String aud;
    long reference;
    public  static    ImageView implay;
    public static   ImageView mImageView;
    Snackbar snackbar;
    String path = Constant.BASE_URL+"uploads/audio/";
    CheckAndRequestPermission cr;
    DatabaseHelper dh;
    ArrayList<TableModel> storedArray;
    String today_date;
    Date compare_date;
    SimpleDateFormat df;
    Calendar c;
    public boolean[] clickStatus;
    public RVAdapterAudioList(List<AudioModel> audioModelArrayList, Context con) {
        this.context = con;


        this.audioModelArrayList = audioModelArrayList;
        itemsCopy.addAll(audioModelArrayList);
        clickStatus=new boolean[audioModelArrayList.size()];
        cr=new CheckAndRequestPermission();
        dh=new DatabaseHelper(context);
        for(AudioModel am:itemsCopy){
            System.out.println("----------------------------------------------------------");
            System.out.println("getAudio : "+am.getAudio());
            System.out.println("getCategory_id : "+am.getCategory_id());
            System.out.println("getFilter_content : "+am.getFilter_content());
            System.out.println("getOrator : "+am.getOrator());
            System.out.println("getProgramme : "+am.getProgramme());
            System.out.println("getSubject : "+am.getSubject());
            System.out.println("getSub_category_id : "+am.getSub_category_id());
            System.out.println("getTitle : "+am.getTitle());
            System.out.println("----------------------------------------------------------");

        }

        storedArray=dh.getAllDatas();
        System.out.println("inside table - names : ");
        for(TableModel tm:storedArray){
            System.out.println(tm.getName()+" ----- "+tm.getDate());
        }
try {
    c = Calendar.getInstance();
    df = new SimpleDateFormat("yyyy-MM-dd");
    today_date = df.format(c.getTime());
    compare_date = df.parse(today_date);
}catch (Exception e){
    e.printStackTrace();
}

    }


    public  class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView subcategry_audio_name;
        TextView subcategory_orator;
        LinearLayout subcategory_news;
        ImageView subcategory_audio_thumbnail;
        ImageView downloads;



        ProgressBar progress;

        LinearLayout  mySecondView;
        LinearLayout lin_download, popup_click;


        public PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            subcategry_audio_name = (TextView) itemView.findViewById(R.id.subcategory_audio_title);
            subcategory_orator = (TextView) itemView.findViewById(R.id.subcategory_orator);
            subcategory_audio_thumbnail = (ImageView) itemView.findViewById(R.id.Subcategry_audio_thumbnail);
            lin_download = (LinearLayout) itemView.findViewById(R.id.lin_download);
            popup_click = (LinearLayout) itemView.findViewById(R.id.popup_click);
            mySecondView = (LinearLayout) itemView.findViewById(R.id.view2);
            downloads = (ImageView) itemView.findViewById(R.id.download);
            mImageView = (ImageView) itemView.findViewById(R.id.coverImage);

            progress = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            subcategory_news = (LinearLayout) itemView.findViewById(R.id.subcategory_news);

            AudioFragment.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            implay = (ImageView) itemView.findViewById(R.id.play);



            final Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Lato-Medium.ttf");
            subcategry_audio_name.setTypeface(typeface);
        }

    }


    @Override
    public int getItemCount() {
        return audioModelArrayList.size();
    }




    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem2, viewGroup, false);

        PersonViewHolder pvh = new PersonViewHolder(v);

        System.out.println("categry id" + audioModelArrayList.get(i).getCategory_id());

        return pvh;

    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {



        personViewHolder.subcategry_audio_name.setText(audioModelArrayList.get(i).getSubject());

        personViewHolder.subcategory_orator.setText(audioModelArrayList.get(i).getOrator());


        System.out.println("categry id" + audioModelArrayList.get(i).getCategory_id());
        System.out.println("subcategryname" + audioModelArrayList.get(i).getAudio());
        audio_url = audioModelArrayList.get(i).getThumbnail();
        System.out.println("categry id" + audioModelArrayList.get(i).getThumbnail());


try {
    if (audioModelArrayList.get(i).getNEWW().contentEquals("new")) {
        if (clickStatus[i] == true) {//to prevent 'subcategory_news' box showing up again after scrolling recyclerview
            personViewHolder.subcategory_news.setVisibility(View.GONE);
        } else {
            personViewHolder.subcategory_news.setVisibility(View.VISIBLE);
        }

        for (TableModel tm : storedArray) {
            if (tm.getName().equalsIgnoreCase(audioModelArrayList.get(i).getSubject().replaceAll("\\s+", ""))) {
                personViewHolder.subcategory_news.setVisibility(View.GONE);
                System.out.println("tm.getName() : " + tm.getName());
                System.out.println("audioModelArrayList.get(i).getSubject() : " + audioModelArrayList.get(i).getSubject().replaceAll("\\s+", ""));
            }
            try {
                Date db_date = df.parse(tm.getDate());

                Calendar c = Calendar.getInstance();
                c.setTime(db_date);
                c.add(Calendar.DATE, 7);  // number of days to add
                String added_date = df.format(c.getTime());  // dt is now the new date
                db_date = df.parse(added_date);
                /////////////////////////////////////////////
                //for testing if table rows are deleted
                    /*Calendar c2 = Calendar.getInstance();
                    c2.setTime(compare_date);
                    c2.add(Calendar.DATE, 8);  // number of days to add
                    String testr_comp_date_date = df.format(c2.getTime());  // dt is now the new date
                    Date test_cmp=df.parse(testr_comp_date_date);*/
                ////////////////////////////////////////////
                System.out.println("compare_date : " + compare_date);
                System.out.println("db_date : " + db_date);

                if (compare_date.after(db_date)) {
                    dh.DeleteRow(tm.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    } else {
        personViewHolder.subcategory_news.setVisibility(View.GONE);
    }
}catch (Exception e){
    e.printStackTrace();
}


        Picasso.with(context).load(audioModelArrayList.get(i).getThumbnail()).into(personViewHolder.subcategory_audio_thumbnail);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        personViewHolder.progress.setTag(i);
        personViewHolder.popup_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStatus[i]=true;
                personViewHolder.subcategory_news.setVisibility(View.GONE);
                if(audioModelArrayList.get(i).getNEWW().contentEquals("new")) {
                    try {
                        System.out.println("clicked audio : " + audioModelArrayList.get(i).getSubject().replaceAll("\\s+", ""));

                        if (cr.checkAndRequestPermissions((Activity) context)) {
                            if (dh.checkExistance(audioModelArrayList.get(i).getSubject().replaceAll("\\s+", "")) > 0) {
                                System.out.println("already added to db table");
                            } else {
                                dh.insertData(audioModelArrayList.get(i).getSubject().replaceAll("\\s+", ""), today_date);
                            }

                        } else {
                            cr.checkAndRequestPermissions((Activity) context);
                        }


                        snackbar.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View layout = inflater.inflate(R.layout.popp, (ViewGroup) view.findViewById(R.id.popup_element), false);

                final PopupWindow pwindo = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

                pwindo.setAnimationStyle(R.style.DialogAnimation);

                cancel = (TextView) layout.findViewById(R.id.cancel7);
                share = (RelativeLayout) layout.findViewById(R.id.share);
                play = (RelativeLayout) layout.findViewById(R.id.play);
                TextView category = (TextView) layout.findViewById(R.id.category);
                TextView share = (TextView) layout.findViewById(R.id.textView4);
                TextView play = (TextView) layout.findViewById(R.id.textView6);
                TextView cancel = (TextView) layout.findViewById(R.id.cancel7);
                LinearLayout share_layout=(LinearLayout)layout.findViewById(R.id.share_layout);
                LinearLayout play_layout=(LinearLayout)layout.findViewById(R.id.play_layout);


                category.setTextColor(context.getResources().getColor(R.color.white));
                category.setTypeface(null, Typeface.BOLD);
                share.setTypeface(null, Typeface.BOLD);
                play.setTypeface(null, Typeface.BOLD);
                cancel.setTypeface(null, Typeface.BOLD);
                category.setText(audioModelArrayList.get(i).getAudio());
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        pwindo.dismiss();
                    }
                });
                share_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AudioFragment.audioFile = audioModelArrayList.get(i).getAudio();

                        Intent emailIntent = new Intent();
                        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("Native"));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                        emailIntent.setType("message/rfc822");

                        PackageManager pm = context.getPackageManager();
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");


                        Intent openInChooser = Intent.createChooser(emailIntent, "Choose");

                        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
                        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
                        for (int i = 0; i < resInfo.size(); i++) {
                            ResolveInfo ri = resInfo.get(i);
                            String packageName = ri.activityInfo.packageName;
                            if(packageName.contains("android.email")) {
                                emailIntent.setPackage(packageName);
                            } else if(packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("mms") || packageName.contains("android.gm")) {
                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                                intent.setAction(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                if(packageName.contains("twitter")) {
                                    intent.putExtra(Intent.EXTRA_TEXT, path+"/"+ AudioFragment.audioFile);
                                } else if(packageName.contains("facebook")) {
                                    // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
                                    // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
                                    // will show the <meta content ="..."> text from that page with our link in Facebook.
                                    intent.putExtra(Intent.EXTRA_TEXT, path+"/"+ AudioFragment.audioFile);
                                } else if(packageName.contains("mms")) {
                                    intent.putExtra(Intent.EXTRA_TEXT, "SMS");
                                } else if(packageName.contains("android.gm")) { // If Gmail shows up twice, try removing this else-if clause and the reference to "android.gm" above
                                    intent.putExtra(Intent.EXTRA_TEXT, path+"/"+ AudioFragment.audioFile);
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                    intent.setType("message/rfc822");
                                }

                                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
                            }
                        }

                        // convert intentList to array
                        LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

                        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
                        // return;
                        context.startActivity(openInChooser);


                    }
                });
                play_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pwindo.dismiss();
                        AudioFragment.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        AudioFragment.mediaPlayer.reset();

                        Picasso.with(context).load(audioModelArrayList.get(i).getThumbnail()).into(AudioFragment.mImageView);
                        AudioFragment.bottomplay.setVisibility(View.VISIBLE);
                        AudioFragment.now_playing_audio.setText(audioModelArrayList.get(i).getSubject());
                        Intent serviceIntent = new Intent(context, NotificationService.class);
                        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                        context.startService(serviceIntent);
                        AudioFragment.audioFile = audioModelArrayList.get(i).getAudio();
                        try {

                            AudioFragment.mediaPlayer.setDataSource(path + "/" + AudioFragment.audioFile);
                            System.out.println("path" + path + "/" + AudioFragment.audioFile);
                            AudioFragment.implay.setBackgroundResource(R.drawable.new_pause);
                            AudioFragment.mediaPlayer.prepareAsync();
                            System.out.println("clickd3");

                        } catch (IOException e) {
                            e.printStackTrace();


                            System.out.println("#############################");
                        }
                        AudioFragment.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);



                    }
                });

                pwindo.showAsDropDown(layout, 50, 30);


            }


        });


        personViewHolder.lin_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    snackbar.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
                v=view;
                personViewHolder.cv.setVisibility(View.VISIBLE);


                personViewHolder.downloads.setVisibility(View.INVISIBLE);
                personViewHolder.progress.setVisibility(View.VISIBLE);


                aud = audioModelArrayList.get(i).getAudio();
                System.out.println("aud : "+aud);
                String url = path + "/" + aud;
                String servicestring = Context.DOWNLOAD_SERVICE;
                DownloadManager downloadmanager;
                downloadmanager = (DownloadManager) context.getSystemService(servicestring);
                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/VoiceOfIslam_Downloads", aud);

                reference = downloadmanager.enqueue(request);
                System.out.println("success" + reference);
                DownloadManager.Query ImageDownloadQuery = new DownloadManager.Query();
                //set the query filter to our previously Enqueued download
                ImageDownloadQuery.setFilterById(reference);

                //Query the download manager about downloads that have been requested.
                Cursor cursor = downloadmanager.query(ImageDownloadQuery);
                if (cursor.moveToFirst()) {
                    String status = DownloadStatus(cursor, reference,view);
                    System.out.println("statuss" + status);
                    // if(status)

                }

                BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

                    @Override
                    public void onReceive(final Context context, Intent intent) {

                        //check if the broadcast message is for our Enqueued download
                        long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                        if (referenceId == reference) {


                            snackbar.dismiss();
                            snackbar= Snackbar.make(v,"Download Complete",Snackbar.LENGTH_INDEFINITE)
                                    .setAction("View", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i=new Intent(context, ListMp3.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(i);
                                        }
                                    });
                            /* Changing message text color*/
                            snackbar.setActionTextColor(Color.parseColor("#91BFDA"));

                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            snackbar.show();
                            {
                                personViewHolder.progress.setVisibility(View.INVISIBLE);
                                personViewHolder.mySecondView.setVisibility(View.VISIBLE);


                            }
                        }

                    }
                };

                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                context.registerReceiver(downloadReceiver, filter);
            }
        });
    }


    private String DownloadStatus(Cursor cursor, long DownloadId,View v){

        //column for download  status
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        //column for reason code if the download failed or paused
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);
        //get the download filename
          String statusText = "";
        String reasonText = "";

        switch(status){
            case DownloadManager.STATUS_FAILED:
                statusText = "STATUS_FAILED";
                switch(reason){
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "ERROR_CANNOT_RESUME";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "ERROR_DEVICE_NOT_FOUND";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "ERROR_FILE_ERROR";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "ERROR_HTTP_DATA_ERROR";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "ERROR_INSUFFICIENT_SPACE";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "STATUS_PAUSED";
                switch(reason){
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = "PAUSED_UNKNOWN";
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = "PAUSED_WAITING_TO_RETRY";
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "STATUS_PENDING";
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = "STATUS_RUNNING";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "STATUS_SUCCESSFUL";
                reasonText = "Filename:\n" + aud;
                break;
        }


        if (DownloadId == reference) {


            if(statusText.equalsIgnoreCase("STATUS_PENDING")||statusText.equalsIgnoreCase("STATUS_RUNNING")) {
                snackbar = Snackbar.make(v, "Downloading...", Snackbar.LENGTH_INDEFINITE);
            }else if(statusText.equalsIgnoreCase("STATUS_PAUSED")) {
                snackbar = Snackbar.make(v, "Paused", Snackbar.LENGTH_INDEFINITE);
            }
            else if(statusText.equalsIgnoreCase("STATUS_FAILED")) {
                snackbar = Snackbar.make(v, "Failed", Snackbar.LENGTH_SHORT);
            }


            snackbar.show();


        }






        // Make a delay of 3 seconds so that next toast (Music Status) will not merge with this one.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 3000);
        return statusText;



    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public AudioModel removeItem(int position)
    {
        final AudioModel model = audioModelArrayList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, AudioModel model)
    {
        audioModelArrayList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition)
    {
        final AudioModel model = audioModelArrayList.remove(fromPosition);
        audioModelArrayList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }






    public  void animateTo(List<AudioModel> models)
    {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private  void applyAndAnimateRemovals(List<AudioModel> newModels)
    {
        for (int i = audioModelArrayList.size() - 1; i >= 0; i--) {
            final AudioModel model =  audioModelArrayList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<AudioModel> newModels)
    {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final AudioModel model = newModels.get(i);
            if (! audioModelArrayList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<AudioModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final AudioModel model = newModels.get(toPosition);
            final int fromPosition =  audioModelArrayList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }}

    public void filter(String text) {

        if(text.isEmpty()){
            {
                audioModelArrayList.clear();
                audioModelArrayList.addAll(itemsCopy);
            }
        } else{
            List<AudioModel> audioModelArrayList1 =new ArrayList<>();
            text = text.toLowerCase();
            for(AudioModel item:itemsCopy ){
                if(item.getSubject().toLowerCase().contains(text)||item.getSubject().toUpperCase().contains(text)||item.getOrator().toLowerCase().contains(text)||item.getOrator().toUpperCase().contains(text) ){
                    audioModelArrayList1.add(item);
                }
            }
            audioModelArrayList.clear();
            audioModelArrayList.addAll(audioModelArrayList1);
        }
        notifyDataSetChanged();
    }


}


