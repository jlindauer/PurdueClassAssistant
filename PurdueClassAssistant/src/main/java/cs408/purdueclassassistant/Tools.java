package cs408.purdueclassassistant;

import java.util.Calendar;

/**
 * Created by Travis on 2/19/14.
 * <p/>
 * Class to call static methods
 */
public class Tools {

    private Tools() {
        // static class
    }

    /**
     * Format time in milliseconds to human readable text
     */
    public static String formatTime(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);

        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);

        if (hour == 0)
            hour = 12;

        String am_pm = "";
        if (c.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "am";
        else
            am_pm = "pm";

        return String.valueOf(hour) + ":" + String.format("%02d", minute) + am_pm;
    }

    /**
     * Format date in milliseconds to human readable text
     */
    public static String formatDateSmall(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);

        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return String.valueOf(month + 1) + "/" + String.valueOf(day);
    }

}
