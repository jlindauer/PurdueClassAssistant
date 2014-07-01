package cs408.purdueclassassistant;

/**
 * Created by Jared on 2/19/14.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class ScheduleDialogFragment extends DialogFragment {

    Context mContext;

    public ScheduleDialogFragment(Activity a) {
        mContext = a;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Schedule");
        alertDialogBuilder.setMessage("Would you like to sync these tasks to your calendar?");
        //null should be your on click listener
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContentResolver contentResolver = mContext.getContentResolver();
                TimeZone timeZone = TimeZone.getDefault();
                Calendar beginTime = Calendar.getInstance();
                Calendar endTime = Calendar.getInstance();
                long startMillis, endMillis;

                InfoCenter info = new InfoCenter(mContext);
                ArrayList<Task> tasks = info.getAllTasks();
                ArrayList<Class> classes = info.getAllClasses();
                ArrayList<Exam> exams = info.getAllExams();
                ArrayList<Homework> homeworks = info.getAllHomework();

                for (Task current : tasks) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    String[] time = sdf.format(current.getTime()).split("/");
                    beginTime.set(Integer.parseInt(time[0]), Integer.parseInt(time[1]) - 1, Integer.parseInt(time[2]), 12, 0);
                    startMillis = beginTime.getTimeInMillis();

                    ContentValues task = new ContentValues();
                    task.put(CalendarContract.Events.TITLE, current.getDescription());
                    task.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
                    task.put(CalendarContract.Events.DESCRIPTION, "Task");
                    task.put(CalendarContract.Events.DTSTART, startMillis);
                    task.put(CalendarContract.Events.DTEND, startMillis);
                    task.put(CalendarContract.Events.EVENT_COLOR, Color.GREEN);
                    task.put(CalendarContract.Events.CALENDAR_ID, 1);
                    contentResolver.insert(CalendarContract.Events.CONTENT_URI, task);
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/hh/mm");

                for (Class current : classes) {
                    String[] startTime = sdf.format(current.getStart()).split("/");
                    beginTime.set(Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]) - 1, current.getDay() + 1, Integer.parseInt(startTime[2]), Integer.parseInt(startTime[3]));
                    startMillis = beginTime.getTimeInMillis();

                    String[] lastTime = sdf.format(current.getEnd()).split("/");
                    endTime.set(Integer.parseInt(lastTime[0]), Integer.parseInt(lastTime[1]) - 1, current.getDay() + 1, Integer.parseInt(lastTime[2]), Integer.parseInt(lastTime[3]));
                    endMillis = endTime.getTimeInMillis();

                    ContentValues curr_class = new ContentValues();
                    curr_class.put(CalendarContract.Events.TITLE, current.getName());
                    curr_class.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
                    curr_class.put(CalendarContract.Events.DESCRIPTION, "\nRoom: " + current.getTeacher() + "\nTeacher: " + current.getRoom() + "\nDescription: " + current.getDesc());
                    curr_class.put(CalendarContract.Events.DTSTART, startMillis);
                    curr_class.put(CalendarContract.Events.DTEND, endMillis);
                    curr_class.put(CalendarContract.Events.EVENT_COLOR, Color.BLUE);
                    curr_class.put(CalendarContract.Events.RRULE, "FREQ=WEEKLY;COUNT=16");
                    //curr_class.put(CalendarContract.Reminders.MINUTES, current.getReminder());
                    curr_class.put(CalendarContract.Events.CALENDAR_ID, 1);
                    contentResolver.insert(CalendarContract.Events.CONTENT_URI, curr_class);
                }

                sdf = new SimpleDateFormat("yyyy/MM/dd/hh/mm");

                for (Exam current : exams) {
                    String[] startTime = sdf.format(current.getTime()).split("/");
                    beginTime.set(Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]) - 1, Integer.parseInt(startTime[2]), Integer.parseInt(startTime[3]), Integer.parseInt(startTime[4]));
                    startMillis = beginTime.getTimeInMillis();

                    ContentValues exam = new ContentValues();
                    exam.put(CalendarContract.Events.TITLE, current.getName());
                    exam.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
                    exam.put(CalendarContract.Events.DESCRIPTION, current.getRoom());
                    exam.put(CalendarContract.Events.DTSTART, startMillis);
                    exam.put(CalendarContract.Events.DTEND, startMillis);
                    exam.put(CalendarContract.Events.EVENT_COLOR, Color.RED);
                    //exam.put(CalendarContract.Reminders.MINUTES, current.getReminder());
                    exam.put(CalendarContract.Events.CALENDAR_ID, 1);
                    contentResolver.insert(CalendarContract.Events.CONTENT_URI, exam);
                }

                for (Homework current : homeworks) {
                    String[] startTime = sdf.format(current.getDue()).split("/");
                    beginTime.set(Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]) - 1, Integer.parseInt(startTime[2]), Integer.parseInt(startTime[3]), Integer.parseInt(startTime[4]));
                    startMillis = beginTime.getTimeInMillis();

                    ContentValues homework = new ContentValues();
                    homework.put(CalendarContract.Events.TITLE, current.getHwname());
                    homework.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
                    homework.put(CalendarContract.Events.DESCRIPTION, current.getCname());
                    homework.put(CalendarContract.Events.DTSTART, startMillis);
                    homework.put(CalendarContract.Events.DTEND, startMillis);
                    homework.put(CalendarContract.Events.EVENT_COLOR, Color.LTGRAY);
                    //homework.put(CalendarContract.Reminders.MINUTES, current.getReminder());
                    homework.put(CalendarContract.Events.CALENDAR_ID, 1);
                    contentResolver.insert(CalendarContract.Events.CONTENT_URI, homework);
                }

                dialog.dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }
}
