package com.sharing.file.data.ftp.transfer.free.wifi.gui;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.sharing.file.data.ftp.transfer.wifi.server.free.R;
import com.sharing.file.data.ftp.transfer.free.wifi.FtpServerService;

public class FsWidgetProvider extends AppWidgetProvider {

    private static final String TAG = FsWidgetProvider.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "Received broadcast: " + intent.getAction());
        // watch for the broadcasts by the ftp server and update the widget if needed
        final String action = intent.getAction();
        if (action.equals(FtpServerService.ACTION_STARTED)
                || action.equals(FtpServerService.ACTION_STOPPED)) {
            Intent updateIntent = new Intent(context, UpdateService.class);
            context.startService(updateIntent);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        Log.d(TAG, "updated called");
        // let the updating happen by a service
        Intent intent = new Intent(context, UpdateService.class);
        context.startService(intent);
    }
    
    public static class UpdateService extends Service {
        // all real work is done in a service to avoid ANR messages
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.d(TAG, "UpdateService start command");
            // We need to create the correct pending intent for when the widget is clicked
            final String action = FtpServerService.isRunning() ? FtpServerService.ACTION_STOP_FTPSERVER
                    : FtpServerService.ACTION_START_FTPSERVER;
            Intent startIntent = new Intent(action);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                    startIntent, 0);
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_layout);
            views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);
            // we need to put the correct image on the widget
            
           
            // new info is on widget, update it
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            ComponentName widget = new ComponentName(this, FsWidgetProvider.class);
            manager.updateAppWidget(widget, views);
            // service has done it's work, android may kill it
            return START_NOT_STICKY;
        }
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
