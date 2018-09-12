package com.meridian.voiceofislam.videofield;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meridian.voiceofislam.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Libin_Cybraum on 6/27/2016.
 */
public class RVAdapter1 extends RecyclerView.Adapter<ItemViewHolder1> {

    private List<CountryModel1> mCountryModel1;
    private List<CountryModel1> mOriginalCountryModel1;
    Context cx;
    public RVAdapter1(List<CountryModel1> mCountryModel1, Context context) {
        this.mCountryModel1 = mCountryModel1;
        this.mOriginalCountryModel1 = mCountryModel1;
        this.cx=context;
    }

    public RVAdapter1() {
    }

    @Override
    public void onBindViewHolder(ItemViewHolder1 itemViewHolder1, int i) {
        final CountryModel1 model1 = mCountryModel1.get(i);

        itemViewHolder1.SUBJECT.setText( model1.getSubject());
        itemViewHolder1.PERSON_NAME.setText( model1.getPersonname());

        if ( model1.getThumnail().isEmpty()) {
            itemViewHolder1.youTubeThumbnailView.setImageResource(R.drawable.ic_launcher);
        } else{
            Picasso.with(cx).load( model1.getThumnail()).noFade().into(itemViewHolder1.youTubeThumbnailView);
        }
        itemViewHolder1.bind(model1,cx);
    }

    @Override
    public ItemViewHolder1 onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
        return new ItemViewHolder1(view);
    }

    @Override
    public int getItemCount() {
        return mCountryModel1.size();
    }

    public void setFilter(List<CountryModel1> countryModels){
        mCountryModel1 = new ArrayList<>();
        mCountryModel1.addAll(countryModels);
        notifyDataSetChanged();
    }

}
