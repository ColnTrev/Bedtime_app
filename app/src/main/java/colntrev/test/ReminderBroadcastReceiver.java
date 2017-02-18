// ReminderBroadcastReceiver.java
// Thy Vu

package colntrev.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    public ReminderBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Log.d("nete", "!!");
        Toast.makeText(context, "30 min to bed time", Toast.LENGTH_SHORT).show();


        createReminder(context, "Bedtime!", "30 min to bedtime. Brush your teeth.");

        Log.d("nete", "reminder!");
        /*
        PowerManager pm  = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"");
        wakeLock.acquire();
        wakeLock.release();
        */
    }

    private void createReminder(Context context, String msg, String msgDetail) {


    }
}
