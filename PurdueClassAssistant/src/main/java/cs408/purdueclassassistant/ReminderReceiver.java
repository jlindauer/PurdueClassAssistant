package cs408.purdueclassassistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Travis on 3/6/14.
 */
public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("name");
        int reminder = intent.getIntExtra("reminder", -1);

        if (reminder == -1)
            return;

        String minStr = " minute";
        if (reminder > 1)
            minStr += "s";

        NotificationController.sendNotification(context, name, "Begins in " + reminder + minStr);
    }
}
