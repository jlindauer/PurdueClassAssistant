package cs408.purdueclassassistant;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


/**
 * Created by frickm on 2/19/14.
 * edited by Travis
 */
public class AddClassFragment extends Fragment {

    private Activity activity;
    private Intent intent;

    private EditText etName, etTeacher, etDesc, etRoom, etReminder;
    private Button dayButton, startTimeButton, endTimeButton, addButton;
    private CheckBox cbReminder;

    private long startTime, endTime;
    private String name, room, teacher, desc;
    private int reminder, index, day;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = activity;
        this.intent = activity.getIntent();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        index = intent.getIntExtra("index", -1);
        day = intent.getIntExtra("day", -1);
        startTime = intent.getLongExtra("start", -1);
        endTime = intent.getLongExtra("end", -1);
        name = intent.getStringExtra("name");
        desc = intent.getStringExtra("description");
        room = intent.getStringExtra("room");
        teacher = intent.getStringExtra("teacher");
        reminder = intent.getIntExtra("reminder", -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_class, container, false);

        etName = (EditText)root.findViewById(R.id.class_name);
        etDesc = (EditText)root.findViewById(R.id.class_desc);
        etRoom = (EditText)root.findViewById(R.id.class_room);
        etTeacher = (EditText)root.findViewById(R.id.class_teacher);
        etReminder = (EditText)root.findViewById(R.id.class_reminder);
        dayButton = (Button)root.findViewById(R.id.class_day_button);
        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDay();
            }
        });
        startTimeButton = (Button)root.findViewById(R.id.class_starttime_button);
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartTime();
            }
        });
        endTimeButton = (Button)root.findViewById(R.id.class_endtime_button);
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEndTime();
            }
        });
        cbReminder = (CheckBox)root.findViewById(R.id.checkbox_class_reminder);

        addButton = (Button)root.findViewById(R.id.addClassButton);
        addButton.setOnClickListener(addListener);

        etName.setText(name);
        etDesc.setText(desc);
        etRoom.setText(room);
        etTeacher.setText(teacher);

        if (reminder != -1) {
            etReminder.setText(String.valueOf(reminder));
            cbReminder.setChecked(true);
        }

        switch (day) {
            case Calendar.SUNDAY:
                dayButton.setText("Sunday");
                break;
            case Calendar.MONDAY:
                dayButton.setText("Monday");
                break;
            case Calendar.TUESDAY:
                dayButton.setText("Tuesday");
                break;
            case Calendar.WEDNESDAY:
                dayButton.setText("Wednesday");
                break;
            case Calendar.THURSDAY:
                dayButton.setText("Thursday");
                break;
            case Calendar.FRIDAY:
                dayButton.setText("Friday");
                break;
            case Calendar.SATURDAY:
                dayButton.setText("Saturday");
                break;
            default:
                break;
        }

        if (startTime != -1 && endTime != -1) {
            startTimeButton.setText(Tools.formatTime(startTime));
            endTimeButton.setText(Tools.formatTime(endTime));
        }

        // Inflate the layout for this fragment
        return root;
    }

    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            name = etName.getText().toString();
            desc = etDesc.getText().toString();
            room = etRoom.getText().toString();
            teacher = etTeacher.getText().toString();

            if (!checkValidity())
                return;

            Intent data = new Intent();
            data.putExtra("INT_TYPE", 0);
            data.putExtra("index", index);
            data.putExtra("day", day);
            data.putExtra("start", startTime);
            data.putExtra("end", endTime);
            data.putExtra("name", name);
            data.putExtra("description", desc);
            data.putExtra("room", room);
            data.putExtra("teacher", teacher);
            data.putExtra("reminder", reminder);

            if (activity.getParent() == null) {
                activity.setResult(Activity.RESULT_OK, data);
            } else {
                activity.getParent().setResult(Activity.RESULT_OK, data);
            }

            activity.finish();
        }
    };

    private boolean checkValidity() {
        if (name == null || name.equals("")) {
            Toast.makeText(activity, "Please enter a class name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (cbReminder.isChecked()) {
            try {
                reminder = Integer.parseInt(etReminder.getText().toString());
            } catch (Exception e) {
                Toast.makeText(activity, "Please enter a valid reminder time", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (reminder < 0) {
                Toast.makeText(activity, "Please enter a valid reminder time", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else
            reminder = -1;

        if (day < Calendar.SUNDAY || day > Calendar.SATURDAY) {
            Toast.makeText(activity, "Please select a valid day", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (startTime < 0) {
            Toast.makeText(activity, "Please enter a valid start time", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (endTime < 0) {
            Toast.makeText(activity, "Please enter a valid end time", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (endTime <= startTime) {
            Toast.makeText(activity, "End time must occur after start time", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void setDay() {
        final String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
                "Friday", "Saturday"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle("Choose a day")
                .setItems(days, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        day = position + 1;
                        dayButton.setText(days[position]);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setStartTime() {
        final Calendar c = Calendar.getInstance();
        final Calendar startCal = Calendar.getInstance();

        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);

        // Create startTime picker
        TimePickerDialog.OnTimeSetListener startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                startCal.set(Calendar.MINUTE, minute);

                startTime = startCal.getTimeInMillis();
                startTimeButton.setText(Tools.formatTime(startTime));
            }
        };
        final TimePickerDialog startTimeSelect = new TimePickerDialog(activity, startTimeSetListener, hour, minute, false);
        startTimeSelect.setTitle("Set start time");
        startTimeSelect.show();
    }

    private void setEndTime() {
        final Calendar c = Calendar.getInstance();
        final Calendar endCal = Calendar.getInstance();

        final int hour = c.get(Calendar.HOUR_OF_DAY) + 1;
        final int minute = c.get(Calendar.MINUTE);

        // Create startTime picker
        TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                endCal.set(Calendar.MINUTE, minute);

                endTime = endCal.getTimeInMillis();
                endTimeButton.setText(Tools.formatTime(endTime));
            }
        };
        final TimePickerDialog endTimeSelect = new TimePickerDialog(activity, endTimeSetListener, hour, minute, false);
        endTimeSelect.setTitle("Set end time");
        endTimeSelect.show();
    }

}
