package cs408.purdueclassassistant;

/**
 * Created by Travis on 2/14/14.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private Activity activity;
    private InfoCenter infoCenter;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    // The fragment argument representing the section number for this fragment.
    private int sectionNumber;
    private static final String ARGS_SECTION_NUMBER = "sectionNumber";

    private View rootView;
    private Task newTask;

    private TaskArrayAdapter taskAdapter;
    private ClassArrayAdapter classAdapter;
    private HomeworkArrayAdapter homeworkAdapter;
    private ExamArrayAdapter examAdapter;
    private MergeAdapter mergeAdapter;

    private Calendar localCal;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        this.infoCenter = new InfoCenter(activity);
        this.sectionNumber = this.getArguments().getInt(ARGS_SECTION_NUMBER);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boolean attachToRoot = false;
        rootView = inflater.inflate(R.layout.fragment_main, container, attachToRoot);

        // Sets the date adjective describing the task list
        TextView textView = (TextView)rootView.findViewById(R.id.title_date_adjective);
        textView.setText(getCurrentDateAdjectiveAndSetCalendar());

        // Needs to be declared
        ListView listView = (ListView)rootView.findViewById(R.id.task_list);

        taskAdapter = new TaskArrayAdapter(activity, new ArrayList<Task>());
        classAdapter = new ClassArrayAdapter(activity, new ArrayList<Class>());
        homeworkAdapter = new HomeworkArrayAdapter(activity, new ArrayList<Homework>());
        examAdapter = new ExamArrayAdapter(activity, new ArrayList<Exam>());

        RelativeLayout header_tasks, header_classes, header_homework, header_exams;
        header_tasks = (RelativeLayout)inflater.inflate(R.layout.header_tasks, null);
        header_classes = (RelativeLayout)inflater.inflate(R.layout.header_classes, null);
        header_homework = (RelativeLayout)inflater.inflate(R.layout.header_homework, null);
        header_exams = (RelativeLayout)inflater.inflate(R.layout.header_exams, null);

        mergeAdapter = new MergeAdapter();
        if (sectionNumber == 5) {
            mergeAdapter.addView(header_tasks, false);
            mergeAdapter.addAdapter(taskAdapter);
            mergeAdapter.addView(header_classes, false);
            mergeAdapter.addAdapter(classAdapter);
            mergeAdapter.addView(header_homework, false);
            mergeAdapter.addAdapter(homeworkAdapter);
            mergeAdapter.addView(header_exams, false);
            mergeAdapter.addAdapter(examAdapter);
        } else if (sectionNumber == 6) {
            mergeAdapter.addView(header_classes, false);
            mergeAdapter.addAdapter(classAdapter);
        } else {
            mergeAdapter.addView(header_tasks, false);
            mergeAdapter.addAdapter(taskAdapter);
            mergeAdapter.addView(header_homework, false);
            mergeAdapter.addAdapter(homeworkAdapter);
            mergeAdapter.addView(header_exams, false);
            mergeAdapter.addAdapter(examAdapter);
        }

        listView.setAdapter(mergeAdapter);
        listView.setOnItemClickListener(listListener);
        listView.setOnItemLongClickListener(listLongListener);

        // initialize lists
        getItemsByDateAdjective(sectionNumber);

        return rootView;
    }

    public void restoreActionBar() {
        ActionBar actionBar = activity.getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Planner");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            inflater.inflate(R.menu.main, menu);
            restoreActionBar();
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                        .setTitle("About")
                        .setMessage("Purdue Class Assistant\n" +
                                "CS408\n\n" +
                                "Michael Frick\nTravis Henning\nJared Lindauer\nTanner McRae");
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.action_edit:
                DialogFragment add_dialog = new AddActivityDialogFragment(activity, this);
                add_dialog.show(getFragmentManager(), "AddActivityDialogFragment");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getCurrentDateAdjectiveAndSetCalendar() {
        String currentTitle;
        localCal = Calendar.getInstance();
        int titleId = sectionNumber;
        switch (titleId) {
            case 1:
                currentTitle = getString(R.string.title_this_week);
                break;
            case 2:
                currentTitle = getString(R.string.title_today);
                break;
            case 3:
                currentTitle = getString(R.string.title_tomorrow);
                localCal.add(Calendar.DAY_OF_MONTH, 1);
                break;
            case 4:
                currentTitle = getString(R.string.title_next_week);
                localCal.add(Calendar.DAY_OF_MONTH, 7);
                break;
            case 5:
                currentTitle = getString(R.string.title_all);
                break;
            case 6:
                currentTitle = "Classes";
                break;
            default:
                currentTitle = "";
        }
        return currentTitle;
    }

    private void getItemsByDateAdjective(int timeId) {
        taskAdapter.clear();
        classAdapter.clear();
        examAdapter.clear();
        homeworkAdapter.clear();

        Calendar def = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        // Note that Calendar handles overflow of fields. ex.) 11/32/2013 = 12/2/13
        switch (timeId) {
            // This week
            case 1:
                taskAdapter.addAll(infoCenter.getTasksForWeek(cal));
                cal.setTimeInMillis(def.getTimeInMillis()); // reset calendar
                examAdapter.addAll(infoCenter.getExamsForWeek(cal));
                cal.setTimeInMillis(def.getTimeInMillis()); // reset calendar
                homeworkAdapter.addAll(infoCenter.getHomeworkForWeek(cal));
                break;
            // Today
            case 2:
                taskAdapter.addAll(infoCenter.getTasksForDay(cal));
                cal.setTimeInMillis(def.getTimeInMillis()); // reset calendar
                examAdapter.addAll(infoCenter.getExamsForDay(cal));
                cal.setTimeInMillis(def.getTimeInMillis()); // reset calendar
                homeworkAdapter.addAll(infoCenter.getHomeworkForDay(cal));
                break;
            // Tomorrow
            case 3:
                cal.add(Calendar.DAY_OF_MONTH, 1);
                taskAdapter.addAll(infoCenter.getTasksForDay(cal));
                cal.setTimeInMillis(def.getTimeInMillis()); // reset calendar
                cal.add(Calendar.DAY_OF_MONTH, 1);
                examAdapter.addAll(infoCenter.getExamsForDay(cal));
                cal.setTimeInMillis(def.getTimeInMillis()); // reset calendar
                cal.add(Calendar.DAY_OF_MONTH, 1);
                homeworkAdapter.addAll(infoCenter.getHomeworkForDay(cal));
                break;
            // Next Week
            case 4:
                cal.add(Calendar.DAY_OF_MONTH, 7);
                taskAdapter.addAll(infoCenter.getTasksForWeek(cal));
                cal.setTimeInMillis(def.getTimeInMillis()); // reset calendar
                cal.add(Calendar.DAY_OF_MONTH, 7);
                examAdapter.addAll(infoCenter.getExamsForWeek(cal));
                cal.setTimeInMillis(def.getTimeInMillis()); // reset calendar
                cal.add(Calendar.DAY_OF_MONTH, 7);
                homeworkAdapter.addAll(infoCenter.getHomeworkForWeek(cal));
                break;
            // All
            case 5:
                taskAdapter.addAll(infoCenter.getAllTasks());
                classAdapter.addAll(infoCenter.getAllClasses());
                examAdapter.addAll(infoCenter.getAllExams());
                homeworkAdapter.addAll(infoCenter.getAllHomework());
                break;
            // Classes
            case 6:
                classAdapter.addAll(infoCenter.getAllClasses());
                break;
            default:
                return;
        }

    }

    private AdapterView.OnItemClickListener listListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Object o = parent.getItemAtPosition(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            if (o instanceof Task) {
                Task task = (Task)o;
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(task.getTime());
                String date = String.valueOf(c.get(Calendar.MONTH) + 1 + "/" +
                        c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR));

                builder.setTitle("Task");
                builder.setMessage("Description: " + task.getDescription() +
                                "\nDate: " + date
                );
            } else if (o instanceof Class) {
                Class c = (Class)o;

                String dayStr = "";
                switch (c.getDay()) {
                    case Calendar.SUNDAY:
                        dayStr = "Sun";
                        break;
                    case Calendar.MONDAY:
                        dayStr = "Mon";
                        break;
                    case Calendar.TUESDAY:
                        dayStr = "Tue";
                        break;
                    case Calendar.WEDNESDAY:
                        dayStr = "Wed";
                        break;
                    case Calendar.THURSDAY:
                        dayStr = "Thur";
                        break;
                    case Calendar.FRIDAY:
                        dayStr = "Fri";
                        break;
                    case Calendar.SATURDAY:
                        dayStr = "Sat";
                        break;
                }

                builder.setTitle("Class");
                builder.setMessage(c.getName() +
                                " (" + dayStr + " " + Tools.formatTime(c.getStart()) +
                                " - " + Tools.formatTime(c.getEnd()) + ")" +
                                "\n" + c.getDesc() +
                                "\n\nRoom: " + c.getRoom() +
                                "\nTeacher: " + c.getTeacher()
                );
            } else if (o instanceof Exam) {
                Exam e = (Exam)o;

                builder.setTitle("Exam");
                builder.setMessage(e.getName() +
                                " (" + Tools.formatTime(e.getTime()) + ")" +
                                "\n\nRoom: " + e.getRoom()
                );
            } else if (o instanceof Homework) {
                Homework hw = (Homework)o;

                builder.setTitle("Homework");
                builder.setMessage(hw.getHwname() +
                                "\nDue: " + Tools.formatDateSmall(hw.getDue()) +
                                "\nClass: " + hw.getCname()
                );
            } else
                throw new ClassCastException("unexpected error");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    private AdapterView.OnItemLongClickListener listLongListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            final Object o = parent.getItemAtPosition(position);
            final String[] options = new String[]{"Edit", "Delete"};

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Options");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    switch (item) {
                        case 0:
                            Intent editIntent = new Intent(activity, EditActivity.class);

                            if (o instanceof Class) {
                                Class c = (Class)o;

                                editIntent.putExtra("INT_TYPE", 0);
                                editIntent.putExtra("description", c.getDesc());
                                editIntent.putExtra("start", c.getStart());
                                editIntent.putExtra("end", c.getEnd());
                                editIntent.putExtra("name", c.getName());
                                editIntent.putExtra("room", c.getRoom());
                                editIntent.putExtra("teacher", c.getTeacher());
                                editIntent.putExtra("reminder", c.getReminder());
                                editIntent.putExtra("day", c.getDay());
                            } else if (o instanceof Homework) {
                                Homework hw = (Homework)o;

                                editIntent.putExtra("INT_TYPE", 1);
                                editIntent.putExtra("name", hw.getHwname());
                                editIntent.putExtra("room", hw.getCname());
                                editIntent.putExtra("due", hw.getDue());
                                editIntent.putExtra("reminder", hw.getReminder());
                            } else if (o instanceof Exam) {
                                Exam exam = (Exam)o;

                                editIntent.putExtra("INT_TYPE", 2);
                                editIntent.putExtra("name", exam.getName());
                                editIntent.putExtra("time", exam.getTime());
                                editIntent.putExtra("room", exam.getRoom());
                                editIntent.putExtra("reminder", exam.getReminder());
                            } else if (o instanceof Task) {
                                Task task = (Task)o;

                                editIntent.putExtra("INT_TYPE", 3);
                                editIntent.putExtra("description", task.getDescription());
                                editIntent.putExtra("start", task.getTime());
                            } else
                                throw new ClassCastException("unexpected error");

                            editIntent.putExtra("index", position);
                            startActivityForResult(editIntent, 1234);

                            break;
                        case 1:
                            if (o instanceof Task) {
                                Task task = (Task)o;
                                taskAdapter.remove(task);
                                infoCenter.removeTask(task);
                            } else if (o instanceof Class) {
                                Class c = (Class)o;
                                classAdapter.remove(c);
                                infoCenter.removeClass(c);
                            } else if (o instanceof Exam) {
                                Exam exam = (Exam)o;
                                examAdapter.remove(exam);
                                infoCenter.removeExam(exam);
                            } else if (o instanceof Homework) {
                                Homework hw = (Homework)o;
                                homeworkAdapter.remove(hw);
                                infoCenter.removeHomework(hw);
                            } else
                                throw new ClassCastException("unexpected error");

                            break;
                        default:
                            break;
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED)
            return;

        int index = data.getIntExtra("index", -1);
        int day = data.getIntExtra("day", -1);
        long startTime = data.getLongExtra("start", -1);
        long endTime = data.getLongExtra("end", -1);
        String name = data.getStringExtra("name");
        String desc = data.getStringExtra("description");
        String room = data.getStringExtra("room");
        String teacher = data.getStringExtra("teacher");
        int reminder = data.getIntExtra("reminder", -1);

        int type = data.getIntExtra("INT_TYPE", -1);
        switch (requestCode) {
            case 1234:
                // EDIT
                Log.d("edit", "edit");

                if (index == -1)
                    return;

                switch (type) {
                    case 0:
                        //CLASS
                        Class c = (Class)mergeAdapter.getItem(index);
                        c.setDay(day);
                        c.setStart(startTime);
                        c.setEnd(endTime);
                        c.setName(name);
                        c.setDesc(desc);
                        c.setRoom(room);
                        c.setTeacher(teacher);
                        c.setReminder(reminder);

                        if (reminder != -1) {
                            ReminderScheduler.cancelReminder(activity, c.getID());
                            ReminderScheduler.scheduleReminder(activity, c);
                        }

                        infoCenter.updateClass(c);
                        classAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        // HOMEWORK
                        Homework hw = (Homework)mergeAdapter.getItem(index);
                        hw.setHwname(name);
                        hw.setCname(room);
                        hw.setDue(endTime);
                        hw.setReminder(reminder);

                        if (reminder != -1) {
                            ReminderScheduler.cancelReminder(activity, hw.getID());
                            ReminderScheduler.scheduleReminder(activity, hw);
                        }

                        infoCenter.updateHomework(hw);
                        homeworkAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        // EXAM
                        Exam exam = (Exam)mergeAdapter.getItem(index);
                        exam.setName(name);
                        exam.setRoom(room);
                        exam.setTime(startTime);
                        exam.setReminder(reminder);

                        if (reminder != -1) {
                            ReminderScheduler.cancelReminder(activity, exam.getID());
                            ReminderScheduler.scheduleReminder(activity, exam);
                        }

                        infoCenter.updateExam(exam);
                        examAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        // TASK
                        Task task = (Task)mergeAdapter.getItem(index);
                        task.setTime(startTime);
                        task.setDescription(desc);

                        infoCenter.updateTask(task);
                        taskAdapter.notifyDataSetChanged();
                        break;
                    default:
                        return;
                }
                break;
            case 4321:
                // ADD
                Log.d("add", "add");
                switch (type) {
                    case 0:
                        //CLASS
                        Class c = new Class(day, startTime, endTime, name, desc, room, teacher,
                                reminder);
                        infoCenter.addClass(c);

                        if (reminder != -1) {
                            ReminderScheduler.cancelReminder(activity, c.getID());
                            ReminderScheduler.scheduleReminder(activity, c);
                        }

                        break;
                    case 1:
                        // HOMEWORK
                        Homework hw = new Homework(endTime, room, name, reminder);
                        infoCenter.addHomework(hw);

                        if (reminder != -1) {
                            ReminderScheduler.cancelReminder(activity, hw.getID());
                            ReminderScheduler.scheduleReminder(activity, hw);
                        }

                        break;
                    case 2:
                        // EXAM
                        Exam exam = new Exam(startTime, name, room, reminder);
                        infoCenter.addExam(exam);

                        if (reminder != -1) {
                            ReminderScheduler.cancelReminder(activity, exam.getID());
                            ReminderScheduler.scheduleReminder(activity, exam);
                        }

                        break;
                    case 3:
                        // TASK
                        Task task = new Task(startTime, desc);
                        infoCenter.addTask(task);
                        break;
                    default:
                        return;
                }
                getItemsByDateAdjective(sectionNumber);
                break;
        }
    }

}
