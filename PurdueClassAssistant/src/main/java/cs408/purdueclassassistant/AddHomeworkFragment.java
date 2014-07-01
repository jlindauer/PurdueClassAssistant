package cs408.purdueclassassistant;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by frickm on 2/28/14.
 * edited by Travis
 */
public class AddHomeworkFragment extends Fragment {

    private Activity activity;
    private Intent intent;

    private EditText etName, etClassName, etReminder;
    private Button endDateButton, addButton;
    private CheckBox cbReminder;

    private long endTime;
    private String name, room;
    private int index, reminder;

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
        endTime = intent.getLongExtra("end", -1);
        room = intent.getStringExtra("room");
        name = intent.getStringExtra("name");
        reminder = intent.getIntExtra("reminder", -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_homework, container, false);

        etClassName = (EditText)root.findViewById(R.id.hw_classname);
        etName = (EditText)root.findViewById(R.id.hw_name);
        endDateButton = (Button)root.findViewById(R.id.due_date_button);
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEndTime();
            }
        });
        etReminder = (EditText)root.findViewById(R.id.homework_reminder);
        cbReminder = (CheckBox)root.findViewById(R.id.checkbox_homework_reminder);

        etClassName.setText(room);
        etName.setText(name);

        if (reminder != -1) {
            etReminder.setText(String.valueOf(reminder));
            cbReminder.setChecked(true);
        }

        if (endTime != -1) {
            endDateButton.setText(Tools.formatDateSmall(endTime) + " - " + Tools.formatTime(endTime));
        }

        addButton = (Button)root.findViewById(R.id.addHwButton);
        addButton.setOnClickListener(addListener);

        // Inflate the layout for this fragment
        return root;
    }

    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            name = etName.getText().toString();
            room = etClassName.getText().toString();

            if (!checkValidity())
                return;

            Intent data = new Intent();
            data.putExtra("INT_TYPE", 1);
            data.putExtra("index", index);
            data.putExtra("end", endTime);
            data.putExtra("name", name);
            data.putExtra("room", room);
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
            Toast.makeText(activity, "Please enter a homework name", Toast.LENGTH_SHORT).show();
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

        if (endTime < 0) {
            Toast.makeText(activity, "Please enter a valid due date", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void setEndTime() {
        final Calendar c = Calendar.getInstance();
        final Calendar endCal = Calendar.getInstance();

        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);

        // Create startTime picker
        TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                endCal.set(Calendar.MINUTE, minute);

                endTime = endCal.getTimeInMillis();
                endDateButton.setText(Tools.formatDateSmall(endTime) + " - " + Tools.formatTime(endTime));
            }
        };
        final TimePickerDialog endTimeSelect = new TimePickerDialog(activity, endTimeSetListener, hour, minute, false);
        endTimeSelect.setTitle("Set end time");

        // initialize date to today
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int month = c.get(Calendar.MONTH);
        final int year = c.get(Calendar.YEAR);

        // create date picker
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                endCal.set(year, month, day);

                endTimeSelect.show();
            }
        };
        DatePickerDialog dateSelect = new DatePickerDialog(activity, dateSetListener, year, month, day);
        dateSelect.show();
    }

}