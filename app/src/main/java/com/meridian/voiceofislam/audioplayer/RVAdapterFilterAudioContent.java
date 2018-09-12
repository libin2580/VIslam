package com.meridian.voiceofislam.audioplayer;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meridian.voiceofislam.Constant;
import com.meridian.voiceofislam.R;
import com.meridian.voiceofislam.lazylist.ImageLoader;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 14-10-2015.
 */
public class RVAdapterFilterAudioContent extends RecyclerView.Adapter<RVAdapterFilterAudioContent.PersonViewHolder>  {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RelativeLayout play, share;
    TextView cancel;
    static Context context;

    ArrayList<AudioFilterContentModel> itemsCopy = new ArrayList<>();
    ArrayList<AudioFilterContentModel> subCategoryModelArrayList;

    String audio_url;

    static String aud;
    long reference;

    public  static    ImageView implay;
    public static   ImageView mImageView;

    String  REGISTER_URL  =Constant.BASE_URL+"services/response.php";
    String path = Constant.BASE_URL+"uploads/audio/";
    public static RVAdapterFilterAudioContent newInstance(String param1, String param2) {
        AudioFragment fragment = new AudioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return null;
    }
    public RVAdapterFilterAudioContent(ArrayList<AudioFilterContentModel> subCategoryModelArrayList, Context con) {
        this.context = con;
        this.subCategoryModelArrayList=subCategoryModelArrayList;


        itemsCopy.addAll(subCategoryModelArrayList);

    }


    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView subcategry_audio_name;

        ImageView subcategory_audio_thumbnail;
        ImageView downloads;


        ProgressBar progress;
        LinearLayout  mySecondView;
        LinearLayout lin_download, popup_click;

        public PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            subcategry_audio_name = (TextView) itemView.findViewById(R.id.subcategory_audio_title);

            subcategory_audio_thumbnail = (ImageView) itemView.findViewById(R.id.Subcategry_audio_thumbnail);
            lin_download = (LinearLayout) itemView.findViewById(R.id.lin_download);
            popup_click = (LinearLayout) itemView.findViewById(R.id.popup_click);
            mySecondView = (LinearLayout) itemView.findViewById(R.id.view2);
            downloads = (ImageView) itemView.findViewById(R.id.download);
            mImageView = (ImageView)itemView. findViewById(R.id.coverImage);
            progress = (ProgressBar) itemView.findViewById(R.id.progress_bar);

            AudioFragment.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            implay= (ImageView)itemView.findViewById(R.id.play);

            final Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Lato-Medium.ttf");
            subcategry_audio_name.setTypeface(typeface);


        }
    }


    @Override
    public int getItemCount() {
        return subCategoryModelArrayList.size();
    }




    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem3, viewGroup, false);

        PersonViewHolder pvh = new PersonViewHolder(v);

        System.out.println("categry id" + subCategoryModelArrayList.get(i).getAudio());

        return pvh;

    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {
        personViewHolder.subcategry_audio_name.setText(subCategoryModelArrayList.get(i).getTitle());



        System.out.println("categry id" + subCategoryModelArrayList.get(i).getCategory_id());
        System.out.println("subcategryname" + subCategoryModelArrayList.get(i).getAudio());
        audio_url = subCategoryModelArrayList.get(i).getThumbnail();
        System.out.println("categry id" + subCategoryModelArrayList.get(i).getThumbnail());
        ImageLoader imageLoader = new ImageLoader(context);

        imageLoader.DisplayImage(audio_url, personViewHolder.subcategory_audio_thumbnail);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        personViewHolder.progress.setTag(i);
        personViewHolder.popup_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View layout = inflater.inflate(R.layout.popp, (ViewGroup) view.findViewById(R.id.popup_element), false);

                final PopupWindow pwindo = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

                pwindo.showAtLocation(layout, Gravity.CENTER, 10, 10);


                cancel = (TextView) layout.findViewById(R.id.cancel7);
                share = (RelativeLayout) layout.findViewById(R.id.share);
                play = (RelativeLayout) layout.findViewById(R.id.play);
                TextView category = (TextView) layout.findViewById(R.id.category);
                TextView share = (TextView) layout.findViewById(R.id.textView4);
                TextView play = (TextView) layout.findViewById(R.id.textView6);
                TextView cancel = (TextView) layout.findViewById(R.id.cancel7);

                category.setTextColor(context.getResources().getColor(R.color.white));
                category.setTypeface(null, Typeface.BOLD);
                share.setTypeface(null, Typeface.BOLD);
                play.setTypeface(null, Typeface.BOLD);
                cancel.setTypeface(null, Typeface.BOLD);
                category.setText(subCategoryModelArrayList.get(i).getTitle());
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pwindo.dismiss();
                    }
                });
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AudioFragment.audioFile = subCategoryModelArrayList.get(i).getAudio();
                        Resources resources = Resources.getSystem();

                        Intent emailIntent = new Intent();
                        emailIntent.setAction(Intent.ACTION_SEND);
                        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
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
                            // Extract the label, append it, and repackage it in a LabeledIntent
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
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pwindo.dismiss();
                        AudioFragment.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        AudioFragment.mediaPlayer.reset();

                        Picasso.with(context).load(subCategoryModelArrayList.get(i).getThumbnail()).into(AudioFragment.mImageView);
                        AudioFragment.bottomplay.setVisibility(View.VISIBLE);
                        AudioFragment.now_playing_audio.setText(subCategoryModelArrayList.get(i).getTitle());
                        Intent serviceIntent = new Intent(context, NotificationService.class);
                        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                        context.startService(serviceIntent);
                        AudioFragment.audioFile = subCategoryModelArrayList.get(i).getAudio();
                        try {

                            AudioFragment.mediaPlayer.setDataSource(path + "/" + AudioFragment.audioFile);
                            System.out.println("path" + path + "/" + AudioFragment.audioFile);
                            AudioFragment.downplay.setBackgroundResource(R.drawable.ic_action_pause);
                            AudioFragment.implay.setBackgroundResource(R.drawable.new_pause);
                            AudioFragment.mediaPlayer.prepareAsync();
                            System.out.println("clickd3");

                            // might take long! (for buffering, etc)
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


                personViewHolder.cv.setVisibility(View.VISIBLE);


                personViewHolder.downloads.setVisibility(View.INVISIBLE);
                personViewHolder.progress.setVisibility(View.VISIBLE);


                aud = subCategoryModelArrayList.get(i).getAudio();
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
                    String status = DownloadStatus(cursor, reference);
                    System.out.println("statuss" + status);

                }

                BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

                    @Override
                    public void onReceive(Context context, Intent intent) {

                        //check if the broadcast message is for our Enqueued download
                        long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                        if (referenceId == reference) {

                            Toast toast = Toast.makeText(context, aud + "\t" +
                                    "Download Complete", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP, 25, 400);
                            toast.show();
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


    private String DownloadStatus(Cursor cursor, long DownloadId){

        //column for download  status
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        //column for reason code if the download failed or paused
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);
        //get the download filename
        int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
        String filename = cursor.getString(filenameIndex);

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

            Toast toast = Toast.makeText(context,
                    "Music Download Status:" + "\n" + statusText + "\n" +
                            reasonText,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

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

    public AudioFilterContentModel removeItem(int position)
    {
        final AudioFilterContentModel model = subCategoryModelArrayList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, AudioFilterContentModel model)
    {
        subCategoryModelArrayList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition)
    {
        final AudioFilterContentModel model = subCategoryModelArrayList.remove(fromPosition);
        subCategoryModelArrayList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }






    public  void animateTo(List<AudioFilterContentModel> models)
    {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private  void applyAndAnimateRemovals(List<AudioFilterContentModel> newModels)
    {
        for (int i =  subCategoryModelArrayList.size() - 1; i >= 0; i--) {
            final AudioFilterContentModel model =  subCategoryModelArrayList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<AudioFilterContentModel> newModels)
    {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final AudioFilterContentModel model = newModels.get(i);
            if (! subCategoryModelArrayList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<AudioFilterContentModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final AudioFilterContentModel model = newModels.get(toPosition);
            final int fromPosition =  subCategoryModelArrayList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }}

    public void filter(String text) {

        if(text.isEmpty())
        {

                subCategoryModelArrayList.clear();
                subCategoryModelArrayList.addAll(itemsCopy);

        } else
        {
            //  ArrayList<PhoneBookItem> result = new ArrayList<>();
            List<AudioFilterContentModel> subCategoryModelArrayList1=new ArrayList<>();
            text = text.toLowerCase();
            for(AudioFilterContentModel item:itemsCopy ){
                if(item.getTitle()!=null)
                {if(item.getTitle().toLowerCase().contains(text)) {
                    subCategoryModelArrayList1.add(item);
                }
                }
            }
            subCategoryModelArrayList.clear();
            subCategoryModelArrayList.addAll(subCategoryModelArrayList1);
        }
        notifyDataSetChanged();
    }
}


