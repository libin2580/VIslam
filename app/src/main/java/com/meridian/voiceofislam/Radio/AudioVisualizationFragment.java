package com.meridian.voiceofislam.Radio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cleveroad.audiovisualization.AudioVisualization;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.GLAudioVisualizationView;
import com.meridian.voiceofislam.R;

import java.io.FileInputStream;

//  compile 'com.h6ah4i.android:openslmediaplayer:0.7.0'

/**
 * Fragment with visualization of audio output.
 */
public class AudioVisualizationFragment extends Fragment implements AudioVisualization {
	Button buttonPlay;

	public static AudioVisualizationFragment newInstance() {
		return new AudioVisualizationFragment();
	}

	private int progressStatus = 0;
	private AudioVisualization audioVisualization;
	final String audioFile = "http://streams.museter.com:8078/;stream.mp3";
	GLAudioVisualizationView glAudioVisualizationView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_audio_visualisation, container, false);
		glAudioVisualizationView = (GLAudioVisualizationView) view.findViewById(R.id.visualizer_view1);
		audioVisualization = (AudioVisualization) glAudioVisualizationView;
		buttonPlay = (Button) view.findViewById(R.id.butn_play);

		final MediaPlayer mp2 = MediaPlayer.create(getActivity(), Uri.parse(audioFile));
		try {
			Log.d("AudioStartService"," path "  + audioFile);
			FileInputStream is = new FileInputStream(audioFile);
			mp2.setDataSource(is.getFD());
			mp2.prepare();
			mp2.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		buttonPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				progressStatus = 0;
				new Thread(new Runnable() {
					@Override
					public void run() {
						while (progressStatus < 100) {
							// Update the progress status
							progressStatus += 1;

							// Try to sleep the thread for 20 milliseconds
							try {
								Thread.sleep(20);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();

				if (mp2.isPlaying() == true) {    // Pause the music player
					buttonPlay.setBackgroundResource(R.drawable.play_radio);
					mp2.pause();
				} else if (mp2.isPlaying() == false)
				{
					buttonPlay.setBackgroundResource(R.drawable.pause_radio);
					mp2.start();
				}

			}
		});


		return view;
	}


	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);



		if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED)

		{
			audioVisualization.linkTo(DbmHandler.Factory.newVisualizerHandler(getContext(), 0));
		}

		else
		{
			Toast.makeText(getActivity(),"Visualistaion Disabled Due  to Permission Denial",Toast.LENGTH_SHORT).show();
		}

	}


	@Override
	public <T> void linkTo(@NonNull DbmHandler<T> dbmHandler) {

	}

	@Override
	public void onResume() {
		super.onResume();
		audioVisualization.onResume();
	}


	@Override
	public void onDestroyView() {
		audioVisualization.release();
		super.onDestroyView();
	}

	@Override
	public void onPause() {
		audioVisualization.onPause();
		super.onPause();

	}

	@Override
	public void release() {

	}

//	private Notification createNotification() {
//		if (mMetadata == null || mPlaybackState == null) {
//			return null;
//		}
//
//		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mService);
//		int playPauseButtonPosition = 0;
//
//
//		addPlayPauseAction(notificationBuilder);
//
//		MediaDescriptionCompat description = mMetadata.getDescription();
//
//		String fetchArtUrl = null;
//		Bitmap art = null;
//		if (description.getIconUri() != null) {
//			// This sample assumes the iconUri will be a valid URL formatted String, but
//			// it can actually be any valid Android Uri formatted String.
//			// async fetch the album art icon
//			String artUrl = description.getIconUri().toString();
//			art = ChannelImageCache.getInstance().getBigImage(artUrl);
//			if (art == null) {
//				fetchArtUrl = artUrl;
//				// use a placeholder art while the remote art is being downloaded
//				art = BitmapFactory.decodeResource(mService.getResources(),
//						R.drawable.ic_default_art);
//			}
//		}
//
//		notificationBuilder
//				.setStyle(new NotificationCompat.MediaStyle()
//						.setShowActionsInCompactView(
//								new int[]{playPauseButtonPosition})  // show only play/pause in compact view
//						.setMediaSession(mSession.getSessionToken()))
//				.setColor(mNotificationColor)
//				.setSmallIcon(R.drawable.ic_notification)
//				.setVisibility(Notification.VISIBILITY_PUBLIC)
//				.setUsesChronometer(true)
//				.setContentIntent(createContentIntent(description))
//				.setContentTitle(description.getTitle())
//				.setContentText(description.getSubtitle())
//				.setLargeIcon(art);
//
//		setNotificationPlaybackState(notificationBuilder);
//		if (fetchArtUrl != null) {
//			fetchBitmapFromURLAsync(fetchArtUrl, notificationBuilder);
//		}
//
//		return notificationBuilder.build();
//	}
//
//	private void addPlayPauseAction(NotificationCompat.Builder builder) {
//		String label;
//		int icon;
//		PendingIntent intent;
//		if (mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
//			label = mService.getString(R.string.mr_media_route_controller_pause);
//			icon = R.drawable.uamp_ic_pause_white_24dp;
//			intent = mPauseIntent;
//		} else {
//			label = mService.getString(R.string.mr_media_route_controller_play);
//			icon = R.drawable.uamp_ic_play_arrow_white_24dp;
//			intent = mPlayIntent;
//		}
//		builder.addAction(new NotificationCompat.Action(icon, label, intent));
//	}
//
//	private void updateMetadata() {
//
//		final MediaMetadataCompat track = generateMediaMetadataCompat(currentMetaData);
//
//		mediaSession.setMetadata(track);
//	}
//
//	private MediaMetadataCompat generateMediaMetadataCompat(SomeMetadataModelObject streamMetadata) {
//		MediaMetadataCompat.Builder currentMediaMetadataCompat = new MediaMetadataCompat.Builder()
//				.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, streamMetadata.getMediaId())
//				.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, streamMetadata.getAlbumName())
//				.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, streamMetadata.getArtistName())
//
//				.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, "http://somealbumart.png")
//				.putString(MediaMetadataCompat.METADATA_KEY_TITLE, streamMetadata.getSongTitle())
//
//				.build();
//		return currentMediaMetadataCompat;
//	}

}