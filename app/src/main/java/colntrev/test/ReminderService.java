// ReminderService.java
// Thy Vu

package colntrev.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class ReminderService extends Service {
    public ReminderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        // note: consider mNotify.setDeleteIntent(PendingIntent) for setAutoCancel (??)
        Intent intent = new Intent(this.getApplicationContext(), TipsActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Intent intentRecord = new Intent(this.getApplicationContext(), RecordSleepActivity.class);
        PendingIntent pIntentRecord = PendingIntent.getActivity(this, 0 , intentRecord, 0);
        //NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_brightness_3_black_24px,"See Tips",pIntent).build();
        Notification mNotify = new Notification.Builder(this)
                .setContentTitle("Bed time!")
                .setContentText("A good sleep improves memory.")
                .setSmallIcon(R.drawable.ic_brightness_3_white_24px)
                .addAction(0,"Sleep Help", pIntent)
                .addAction(0,"Record",pIntentRecord)
                .setSound(sound)
                .setAutoCancel(true)
                .build();
        NotificationManager notifManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notifManager.notify(1, mNotify);


    }
}
