package cs408.purdueclassassistant;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by frickm on 2/28/14.
 * edited by Travis
 */
public class AddTaskFragment extends Fragment {

    private Activity activity;
    private Intent intent;

    private EditText etDesc;
    private Button startDateButton, addButton;

    private long startTime;
    private String desc;
    private int index;

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
        startTime = intent.getLongExtra("start", -1);
        desc = intent.getStringExtra("description");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_task, container, false);

        addButton = (Button)root.findViewById(R.id.addTaskButton);
        addButton.setOnClickListener(addListener);

        startDateButton = (Button)root.findViewById(R.id.task_date_button);
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartDate();
            }
        });
        etDesc = (EditText)root.findViewById(R.id.task_desc);

        etDesc.setText(desc);

        if (startTime != -1) {
            startDateButton.setText(Tools.formatDateSmall(startTime));
        }

        // Inflate the layout for this fragment
        return root;
    }

    private void setStartDate() {
        final Calendar c = Calendar.getInstance();

        // initialize date to today
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int month = c.get(Calendar.MONTH);
        final int year = c.get(Calendar.YEAR);

        // create date picker
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                c.set(year, month, day);
                startTime = c.getTimeInMillis();
                startDateButton.setText(Tools.formatDateSmall(startTime));
            }
        };
        DatePickerDialog dateSelect = new DatePickerDialog(activity, dateSetListener, year, month, day);
        dateSelect.show();
    }

    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            desc = etDesc.getText().toString();

            Intent data = new Intent();
            data.putExtra("INT_TYPE", 3);
            data.putExtra("index", index);
            data.putExtra("description", desc);
            data.putExtra("start", startTime);

            if (activity.getParent() == null) {
                activity.setResult(Activity.RESULT_OK, data);
            } else {
                activity.getParent().setResult(Activity.RESULT_OK, data);
            }

            activity.finish();
        }
    };

}