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
public class AddExamFragment extends Fragment {

    private Activity activity;
    private Intent intent;

    private EditText etName, etRoom, etReminder;
    private Button startTimeButton, addButton;
    private CheckBox cbReminder;

    private long startTime;
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
        startTime = intent.getLongExtra("time", -1);
        name = intent.getStringExtra("name");
        room = intent.getStringExtra("room");
        reminder = intent.getIntExtra("reminder", -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_exam, container, false);

        startTimeButton = (Button)root.findViewById(R.id.exam_time_button);
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartTime();
            }
        });
        etName = (EditText)root.findViewById(R.id.exam_name);
        etRoom = (EditText)root.findViewById(R.id.exam_room);
        etReminder = (EditText)root.findViewById(R.id.exam_reminder);
        cbReminder = (CheckBox)root.findViewById(R.id.checkbox_exam_reminder);

        etName.setText(name);
        etRoom.setText(room);

        if (reminder != -1) {
            etReminder.setText(String.valueOf(reminder));
            cbReminder.setChecked(true);
        }

        if (startTime != -1) {
            startTimeButton.setText(Tools.formatDateSmall(startTime) + " - " + Tools.formatTime(startTime));
        }

        addButton = (Button)root.findViewById(R.id.addExamButton);
        addButton.setOnClickListener(addListener);

        // Inflate the layout for this fragment
        return root;
    }

    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            name = etName.getText().toString();
            room = etRoom.getText().toString();

            Intent data = new Intent();
            data.putExtra("INT_TYPE", 2);
            data.putExtra("index", index);
            data.putExtra("start", startTime);
            data.putExtra("name", name);
            data.putExtra("room", room);
            data.putExtra("reminder", reminder);

            if (!checkValidity())
                return;

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
            Toast.makeText(activity, "Please enter an exam name", Toast.LENGTH_SHORT).show();
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

        if (startTime < 0) {
            Toast.makeText(activity, "Please enter a valid start time", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
                startTimeButton.setText(Tools.formatDateSmall(startTime) + " - " + Tools.formatTime(startTime));
            }
        };
        final TimePickerDialog startTimeSelect = new TimePickerDialog(activity, startTimeSetListener, hour, minute, false);
        startTimeSelect.setTitle("Set start time");

        // initialize date to today
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int month = c.get(Calendar.MONTH);
        final int year = c.get(Calendar.YEAR);

        // create date picker
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                startCal.set(year, month, day);

                startTimeSelect.show();
            }
        };
        DatePickerDialog dateSelect = new DatePickerDialog(activity, dateSetListener, year, month, day);
        dateSelect.show();
    }

}