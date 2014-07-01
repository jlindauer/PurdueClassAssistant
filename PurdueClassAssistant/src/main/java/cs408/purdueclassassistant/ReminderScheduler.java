package cs408.purdueclassassistant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Travis on 3/6/14.
 */
public class ReminderScheduler {

    private ReminderScheduler() {
        // static class
    }

    public static void scheduleReminder(Context context, Object o) {
        Intent intent = new Intent(context, ReminderReceiver.class);

        String name;
        long time;
        int reminder, id;

        if (o instanceof Class) {
            Class c = (Class)o;
            name = c.getName();
            time = c.getStart();
            reminder = c.getReminder();
            id = c.getID();
        } else if (o instanceof Homework) {
            Homework hw = (Homework)o;
            name = hw.getHwname();
            time = hw.getDue();
            reminder = hw.getReminder();
            id = hw.getID();
        } else if (o instanceof Exam) {
            Exam exam = (Exam)o;
            name = exam.getName();
            time = exam.getTime();
            reminder = exam.getReminder();
            id = exam.getID();
        } else
            throw new ClassCastException("an unexpected error has occurred...");

        intent.putExtra("name", name);
        intent.putExtra("reminder", reminder);

        long reminderInMillis = reminder * 60000;

        // set alarm
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, time - reminderInMillis, sender);
        Log.d("schedule", "Reminder scheduled...");
    }

    public static void cancelReminder(Context context, int requestCode) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
        Log.d("schedule", "Reminder cancelled...");
    }

}
