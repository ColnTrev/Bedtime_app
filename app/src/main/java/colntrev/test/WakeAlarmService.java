package colntrev.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Thy Vu on 2/28/17.
 */


// Notification Service to wake up the user

public class WakeAlarmService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intentRecord = new Intent(this.getApplicationContext(), RecordSleepActivity.class);
        PendingIntent pIntentRecord = PendingIntent.getActivity(this, 0 , intentRecord, 0);
        Notification mNotify = new Notification.Builder(this)
                .setContentTitle("Good morning!")
                .setContentText("How did you sleep last night?")
                .setSmallIcon(R.drawable.ic_wb_sunny_white_24px)
                .addAction(0,"Record",pIntentRecord)
                .setSound(sound)
                .build();
        NotificationManager notifManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notifManager.notify(2, mNotify);

        return Service.START_NOT_STICKY;
    }

    public void onCreate() {
        super.onCreate();

    }
}
