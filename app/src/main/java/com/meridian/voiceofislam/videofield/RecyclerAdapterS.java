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
 * Created by libin on 9/16/2016.
 */
public class RecyclerAdapterS extends RecyclerView.Adapter<ItemViewHolder2> {
    private List<VideoCatModel> catmod;
    private List<VideoCatModel> mOriginalCountryModel1;
    Context cx;
    public RecyclerAdapterS(List<VideoCatModel> catmod, Context context) {
        this.catmod=catmod;
        this.mOriginalCountryModel1=catmod;
        this.cx=context;
    }

    public RecyclerAdapterS() {
    }

    @Override
    public void onBindViewHolder(ItemViewHolder2 itemViewHolder2, int i) {
        final VideoCatModel model2 = catmod.get(i);

        itemViewHolder2.SUBJECT.setText( model2.getSubject());
        itemViewHolder2.PERSON_NAME.setText( model2.getPersonname());

        if ( model2.getThumbnail().isEmpty()) {
            itemViewHolder2.youTubeThumbnailView.setImageResource(R.drawable.ic_launcher);
        } else{
            Picasso.with(cx).load( model2.getThumbnail()).noFade().into( itemViewHolder2.youTubeThumbnailView);
        }
        itemViewHolder2.bind(model2,cx);
    }

    @Override
    public ItemViewHolder2 onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
        return new ItemViewHolder2(view);
    }

    @Override
    public int getItemCount() {
        return catmod.size();
    }

    public void setFilter(List<VideoCatModel> countryModels){
        catmod = new ArrayList<>();
        catmod.addAll(countryModels);
        notifyDataSetChanged();
    }


}