package com.meridian.voiceofislam.videofield;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.meridian.voiceofislam.R;


/**
 * Created by Libin_Cybraum on 6/27/2016.
 */
public class ItemViewHolder1 extends RecyclerView.ViewHolder {

    public TextView SUBJECT,PERSON_NAME;
    public TextView iso_TextView;
    protected FrameLayout relativeLayoutOverYouTubeThumbnailView;
    static ImageView youTubeThumbnailView;
    TextView m;


    public ItemViewHolder1(View itemView) {
        super(itemView);
        itemView.setClickable(true);
        iso_TextView = (TextView) itemView.findViewById(R.id.country_iso);
        m = (TextView) itemView.findViewById(R.id.textView5);

        SUBJECT= (TextView) itemView.findViewById( R.id.subject_video);
        PERSON_NAME= (TextView)itemView.findViewById( R.id.person_name_video);

        relativeLayoutOverYouTubeThumbnailView = (FrameLayout) itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
        youTubeThumbnailView = (ImageView) itemView.findViewById(R.id.youtube_thumbnail);

    }

    public void bind( final  CountryModel1 countryModel1, final Context cx) {

        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
              relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
            }
        };
     youTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("<<<videoooooooooooooo>>>>" +countryModel1.getVideo());
                if(countryModel1.getVideo().isEmpty()){
                    Toast.makeText(cx,"No Video Available",Toast.LENGTH_SHORT).show();
                }else {

                        Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity)cx,"AIzaSyC3K__qVu_f6pccLuKdUflRq033_Nul6Hk",countryModel1.getVideo());
                        cx.startActivity(intent);

                }
            }
        });


    }


}
