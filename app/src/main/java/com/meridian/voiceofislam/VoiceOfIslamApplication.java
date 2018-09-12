package com.meridian.voiceofislam;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Rashid on 6/27/2016.
 */
public class VoiceOfIslamApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("Raleway-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

}
