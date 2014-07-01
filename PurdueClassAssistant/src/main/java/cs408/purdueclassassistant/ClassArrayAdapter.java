package cs408.purdueclassassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Travis on 2/19/14.
 */
public class ClassArrayAdapter extends ArrayAdapter<Class> {

    private final LayoutInflater inflater;

    public ClassArrayAdapter(Context context, ArrayList<Class> classes) {
        super(context, R.layout.item_school_object, classes);

        this.inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemHolder holder;

        if (convertView == null) {
            holder = new ItemHolder();
            convertView = inflater.inflate(R.layout.item_school_object, parent, false);

            holder.icon = (ImageView)convertView.findViewById(R.id.icon);
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.subtitle = (TextView)convertView.findViewById(R.id.subtitle);
            holder.date = (TextView)convertView.findViewById(R.id.date);

            convertView.setTag(holder);
        } else {
            holder = (ItemHolder)convertView.getTag();
        }

        String title, subtitle;
        long startTime;

        Class c = this.getItem(position);
        title = c.getName();
        subtitle = c.getRoom() + " - " + c.getTeacher();
        startTime = c.getStart();

        // set icon and label
        holder.icon.setImageResource(R.drawable.checkmark);
        holder.title.setText(title);
        holder.subtitle.setText(subtitle);

        // set day
        String day = "";
        switch (c.getDay()) {
            case Calendar.SUNDAY:
                day = "Sun";
                break;
            case Calendar.MONDAY:
                day = "Mon";
                break;
            case Calendar.TUESDAY:
                day = "Tue";
                break;
            case Calendar.WEDNESDAY:
                day = "Wed";
                break;
            case Calendar.THURSDAY:
                day = "Thur";
                break;
            case Calendar.FRIDAY:
                day = "Fri";
                break;
            case Calendar.SATURDAY:
                day = "Sat";
                break;
            default:
                break;
        }

        holder.date.setText(day);

        return convertView;
    }

    class ItemHolder {
        ImageView icon;
        TextView title;
        TextView subtitle;
        TextView date;
    }

}
