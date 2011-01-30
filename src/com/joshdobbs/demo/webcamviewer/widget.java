package com.joshdobbs.demo.webcamviewer;

//A simple WebCam Viewer widget
//Josh Dobbs joshdobbs@gmail.com

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

public class widget extends AppWidgetProvider {

	public static String ACTION_WIDGET_GETIMAGE = "getimage";
	public static String ACTION_WIDGET_GETIMAGE_ONE = "getimage1";
	public static String ACTION_WIDGET_GETIMAGE_TWO = "getimage2";
	Bitmap bitmap;
	

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		//declare and initalize remoteview
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					com.joshdobbs.demo.webcamviewer.R.layout.webcamwidget);
			
			//webCamWidgetImage
			//declare a new intent
			Intent active = new Intent(context, widget.class);
			
			//set the action
			active.setAction(ACTION_WIDGET_GETIMAGE);
			
			//declare the pendingintent
			PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		
			//set the onClickPendingIntent. 
			//pass in the control and the pendingIntent
			remoteViews.setOnClickPendingIntent(
					com.joshdobbs.demo.webcamviewer.R.id.webCamWidgetImage,
					actionPendingIntent);
			
			//button1
			//initialize the intent
			active = new Intent(context, widget.class);
			
			//set the action
			active.setAction(ACTION_WIDGET_GETIMAGE_ONE);
			
			//initialize the pending intent
			actionPendingIntent = PendingIntent.getBroadcast(
					context, 0, active, 0);
			
			//set the onClickPending Intent
			remoteViews.setOnClickPendingIntent(
					com.joshdobbs.demo.webcamviewer.R.id.button1,
					actionPendingIntent);
			
			//button 2
			active = new Intent(context, widget.class);
			active.setAction(ACTION_WIDGET_GETIMAGE_TWO);
			actionPendingIntent = PendingIntent.getBroadcast(
					context, 0, active, 0);
			remoteViews.setOnClickPendingIntent(
					com.joshdobbs.demo.webcamviewer.R.id.button2,
					actionPendingIntent);
			
			
			//if the image has'nt loaded don't set it yet
			if (bitmap != null) {
				remoteViews.setImageViewBitmap(
						com.joshdobbs.demo.webcamviewer.R.id.webCamWidgetImage,
						bitmap);
			}

			//update the app widget
			appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
//		}

	}
	

	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);

		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {

			final int appWidgetId = intent.getExtras().getInt(

			AppWidgetManager.EXTRA_APPWIDGET_ID,

			AppWidgetManager.INVALID_APPWIDGET_ID);

			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {

				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else {

			// check, if our Action was called
			if (intent.getAction().toString().equals(ACTION_WIDGET_GETIMAGE)) {
				// start getting new image
				Timer timer = new Timer();
				timer.schedule(new PicTimer(context, appWidgetManager,context.getString(R.string.cam_url)), 1000);
			}

			if (intent.getAction().toString().equals(ACTION_WIDGET_GETIMAGE_ONE)) {
				// start getting new image
				Timer timer = new Timer();
				timer.schedule(new PicTimer(context, appWidgetManager, context
						.getString(R.string.cam_url)), 1000);
			}
			
			if (intent.getAction().toString().equals(ACTION_WIDGET_GETIMAGE_TWO)) {
				// start getting new image
				Timer timer = new Timer();
				timer.schedule(new PicTimer(context, appWidgetManager,context.getString(R.string.cam_url2)), 1000);
			}
			super.onReceive(context, intent);
		}
	}

	


	private class PicTimer extends TimerTask {

		RemoteViews remoteViews;
		AppWidgetManager appWidgetManager;
		ComponentName thisWidget;
		Context ctxtForRun;
		String camUrl;

		public PicTimer(Context context, AppWidgetManager appWidgetManager, String imgUrl) {
			this.appWidgetManager = appWidgetManager;
			ctxtForRun = context;
			camUrl  = imgUrl;
			remoteViews = new RemoteViews(ctxtForRun.getPackageName(),
					com.joshdobbs.demo.webcamviewer.R.layout.webcamwidget);
			thisWidget = new ComponentName(ctxtForRun, widget.class);

			URL imageURL;
			try {
				imageURL = new URL(camUrl);
				URLConnection conn;
				conn = imageURL.openConnection();
				conn.connect();
				final BufferedInputStream bis = new BufferedInputStream(conn
						.getInputStream());

				bitmap = BitmapFactory.decodeStream(bis);

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void run() {

			if (bitmap == null) {
				return;
			}
			remoteViews.setImageViewBitmap(
					com.joshdobbs.demo.webcamviewer.R.id.webCamWidgetImage,
					bitmap);
			appWidgetManager.updateAppWidget(thisWidget, remoteViews);

		}

	}
}
