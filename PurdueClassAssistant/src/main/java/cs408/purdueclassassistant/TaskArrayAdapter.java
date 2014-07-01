package cs408.purdueclassassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by jared on 1/30/13.
 * Edited by Travis
 */
public class TaskArrayAdapter extends ArrayAdapter<Task> {

    private final LayoutInflater inflater;

    public TaskArrayAdapter(Context context, ArrayList<Task> tasks) {
        super(context, R.layout.item_school_object, tasks);

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
        long time;

        Task task = this.getItem(position);
        title = task.getDescription();
        subtitle = String.valueOf(position + 1);
        time = task.getTime();

        // set icon and label
        holder.icon.setImageResource(R.drawable.checkmark);
        holder.title.setText(title);
        holder.subtitle.setText(subtitle);

        // set date
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        String timeStamp = sdf.format(cal.getTime());
        holder.date.setText(timeStamp);

        return convertView;
    }

    class ItemHolder {
        ImageView icon;
        TextView title;
        TextView subtitle;
        TextView date;
    }

}
