package com.meridian.voiceofislam.audioplayer;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meridian.voiceofislam.Constant;
import com.meridian.voiceofislam.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 14-10-2015.
 */
public class RVAdapterFilterAudio extends RecyclerView.Adapter<RVAdapterFilterAudio.PersonViewHolder> {

    static Context context;
    ArrayList<AudioFilterModel> itemsCopy =new ArrayList<>();
    private List<AudioFilterModel> subCategoryAudioFilterModelArrayList;

    String path = Constant.BASE_URL+"uploads/audio/";

    public RVAdapterFilterAudio(ArrayList<AudioFilterModel> subCategoryAudioFilterModelArrayList, Context con) {
        this.context = con;

        this.subCategoryAudioFilterModelArrayList = subCategoryAudioFilterModelArrayList;
        itemsCopy.addAll(subCategoryAudioFilterModelArrayList);

    }


    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView filter_content_id;
        TextView filter_content;



        ProgressBar progress;


        public PersonViewHolder(View itemView) {
            super(itemView);
                filter_content_id = (TextView)itemView.findViewById(R.id.id);
                filter_content = (TextView)itemView.findViewById(R.id.content);
                progress = (ProgressBar) itemView.findViewById(R.id.progress_bar);
           final Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Lato-Medium.ttf");
            filter_content.setTypeface(typeface);

        }
    }


    @Override
    public int getItemCount() {
        return subCategoryAudioFilterModelArrayList.size();
    }




    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sublist_recycler, viewGroup, false);

        PersonViewHolder pvh = new PersonViewHolder(v);



        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {

        personViewHolder.filter_content_id.setText(subCategoryAudioFilterModelArrayList.get(i).getFilter_content_id());
        personViewHolder.filter_content.setText(subCategoryAudioFilterModelArrayList.get(i).getFilter_content());

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public void filter(String text) {

        if(text.isEmpty()){
            {
                subCategoryAudioFilterModelArrayList.clear();
                subCategoryAudioFilterModelArrayList.addAll(itemsCopy);
            }
        } else{
            List<AudioFilterModel> subCategoryAudioFilterModelArrayList1 =new ArrayList<>();
            text = text.toLowerCase();
            for(AudioFilterModel item:itemsCopy ){
                if(item.getFilter_content().toLowerCase().contains(text) ){
                    subCategoryAudioFilterModelArrayList1.add(item);
                }
            }
            subCategoryAudioFilterModelArrayList.clear();
            subCategoryAudioFilterModelArrayList.addAll(subCategoryAudioFilterModelArrayList1);
        }
        notifyDataSetChanged();
    }






}


