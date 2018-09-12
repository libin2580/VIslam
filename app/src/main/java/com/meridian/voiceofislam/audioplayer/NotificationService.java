package com.meridian.voiceofislam.audioplayer;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

import com.meridian.voiceofislam.MainActivity;
import com.meridian.voiceofislam.R;

public class NotificationService extends Service {
    Notification status;
    private final String LOG_TAG = "NotificationService";
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent || null == intent.getAction ()) {
            stopSelf();
            String source = null == intent ? "intent" : "action";
            return START_STICKY;
        }
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            showNotification();


        }
        else if (intent.getAction().equals(Constants.ACTION.PAUSE_ACTION))
        {
            AudioFragment.implay.setBackgroundResource(R.drawable.newplay);
            AudioFragment.mediaPlayer.pause();
            AudioFragment.minimizeplay.setVisibility(View.GONE);
            AudioFragment.downplay.setVisibility(View.VISIBLE);
            AudioFragment.pauseeplay.setVisibility(View.GONE);

        }
        else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {


        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            System.out.println("clickd");
            AudioFragment.downplay.performClick();
            AudioFragment.minimizeplay.setVisibility(View.GONE);
            AudioFragment.downplay.setVisibility(View.GONE);
            AudioFragment.pauseeplay.setVisibility(View.VISIBLE);

        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {

        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {

            stopService(intent);
            stopForeground(true);
            AudioFragment.mediaPlayer.stop();
            stopSelf();

        }
        return START_STICKY;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification() {
// Using RemoteViews to bind custom layouts into Notification
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.status_bar);

// showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
//        bigViews.setImageViewBitmap(R.id.status_bar_album_art,
//                Constants.getDefaultAlbumArt(this));
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, NotificationService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent  pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent pauseIntent = new Intent(this, NotificationService.class);
        pauseIntent.setAction(Constants.ACTION.PAUSE_ACTION);
        PendingIntent ppauseIntent = PendingIntent.getService(this, 0,
                pauseIntent, 0);

        Intent nextIntent = new Intent(this, NotificationService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);


        views.setOnClickPendingIntent(R.id.status_bar_pause,ppauseIntent);

        views.setImageViewResource(R.id.status_bar_play,
                R.drawable.ic_action_play);


        status = new Notification.Builder(this).build();
        status.contentView = views;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.mipmap.ic_launcher;
        status.contentIntent = pendingIntent;



        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);

    }

}