package cs408.purdueclassassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by tannermcrae on 1/30/14.
 * Edited by Travis
 */

public class InfoCenter extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pca_manager";
    private static final int DATABASE_VERSION = 4;

    private static final String TASK_TABLE_NAME = "Tasks";
    private static final String TASK_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " +
            TASK_TABLE_NAME + " (" +
            " TaskId INTEGER PRIMARY KEY," +
            " Description TEXT NOT NULL," +
            " Time LONG NOT NULL)";

    private static final String CLASS_TABLE_NAME = "Classes";
    private static final String CLASS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " +
            CLASS_TABLE_NAME + " (" +
            " CId INTEGER PRIMARY KEY," +
            " CName TEXT," +
            " CDesc TEXT," +
            " CRoom TEXT NOT NULL," +
            " Teacher TEXT," +
            " Reminder INT," +
            " CStart LONG NOT NULL," +
            " CFinish LONG NOT NULL," +
            " CDay INT NOT NULL)";

    private static final String EXAM_TABLE_NAME = "Exams";
    private static final String EXAM_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " +
            EXAM_TABLE_NAME + " (" +
            " EId INTEGER PRIMARY KEY," +
            " EName TEXT," +
            " CRoom TEXT NOT NULL," +
            " ETime LONG NOT NULL," +
            " Reminder INT)";

    private static final String HOMEWORK_TABLE_NAME = "Homework";
    private static final String HOMEWORK_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " +
            HOMEWORK_TABLE_NAME + " (" +
            " HWId INTEGER PRIMARY KEY," +
            " CName TEXT NOT NULL," +
            " HWName TEXT NOT NULL," +
            " Due LONG NOT NULL," +
            " Reminder INT)";

    public InfoCenter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASK_TABLE_CREATE);
        db.execSQL(CLASS_TABLE_CREATE);
        db.execSQL(EXAM_TABLE_CREATE);
        db.execSQL(HOMEWORK_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CLASS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXAM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HOMEWORK_TABLE_NAME);
        onCreate(db);
    }

    /* Task functions */

    public ArrayList<Task> getAllTasks() {
        Task task;
        ArrayList<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT * FROM Tasks";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String description = cursor.getString(1);
                long time = cursor.getLong(2);

                task = new Task(time, description);
                task.setID(id);
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

    public ArrayList<Task> getTasksForDay(Calendar cal) {
        ArrayList<Task> tasks = new ArrayList<Task>();
        int queryYear = cal.get(Calendar.YEAR);
        int queryMonth = cal.get(Calendar.MONTH);
        int queryDay = cal.get(Calendar.DAY_OF_MONTH);

        String selectQuery = "SELECT * FROM Tasks";

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String description = cursor.getString(1);
                long time = cursor.getLong(2);

                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(time);

                if (queryYear == c.get(Calendar.YEAR) &&
                        queryMonth == c.get(Calendar.MONTH) &&
                        queryDay == c.get(Calendar.DAY_OF_MONTH)) {
                    Task task = new Task(time, description);
                    task.setID(id);
                    tasks.add(task);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

    public ArrayList<Task> getTasksForWeek(Calendar cal) {
        ArrayList<Task> tasksForWeek = new ArrayList<Task>();
        int add = 0;
        for (int i = 0; i < 7; i++) {
            cal.add(Calendar.DAY_OF_MONTH, add);
            tasksForWeek.addAll(getTasksForDay(cal));
            add = 1;
        }
        return tasksForWeek;
    }

    public void addTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Description", task.getDescription());
        cv.put("Time", task.getTime());

        database.insert("Tasks", null, cv);

        Cursor c = database.rawQuery("SELECT last_insert_rowid() from " + TASK_TABLE_NAME, null);

        if (c != null) {
            if (c.moveToFirst() == false) {
                c.close();
                database.close();
                return;
            }

            int id = c.getInt(0);
            task.setID(id);
        }

        c.close();

        database.close();
    }

    public void updateTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Description", task.getDescription());
        cv.put("Time", task.getTime());

        database.update("Tasks", cv, "TaskId = ?", new String[]{String.valueOf(task.getID())});
        database.close();
    }

    public void removeTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("Tasks", "TaskId = " + task.getID(), null);
        database.close();
    }

    /* Class functions */

    public ArrayList<Class> getAllClasses() {
        ArrayList<Class> classes = new ArrayList<Class>();
        String selectQuery = "SELECT * FROM Classes";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                String room = cursor.getString(3);
                String teacher = cursor.getString(4);
                int reminder = cursor.getInt(5);
                long startTime = cursor.getLong(6);
                long endTime = cursor.getLong(7);
                int day = cursor.getInt(8);

                Class c = new Class(day, startTime, endTime, name, description, room, teacher, reminder);
                c.setID(id);
                classes.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return classes;
    }

    public void addClass(Class c) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("CName", c.getName());
        cv.put("CDesc", c.getDesc());
        cv.put("CRoom", c.getRoom());
        cv.put("Teacher", c.getTeacher());
        cv.put("Reminder", c.getReminder());
        cv.put("CStart", c.getStart());
        cv.put("CFinish", c.getEnd());
        cv.put("CDay", c.getDay());

        database.insert("Classes", null, cv);

        Cursor cursor = database.rawQuery("SELECT last_insert_rowid() from " + CLASS_TABLE_NAME, null);

        if (cursor != null) {
            if (cursor.moveToFirst() == false) {
                cursor.close();
                database.close();
                return;
            }

            int id = cursor.getInt(0);
            c.setID(id);
        }

        cursor.close();

        database.close();
    }

    public void updateClass(Class c) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("CName", c.getName());
        cv.put("CDesc", c.getDesc());
        cv.put("CRoom", c.getRoom());
        cv.put("Teacher", c.getTeacher());
        cv.put("Reminder", c.getReminder());
        cv.put("CStart", c.getStart());
        cv.put("CFinish", c.getEnd());
        cv.put("CDay", c.getDay());

        database.update("Classes", cv, "CId = ?", new String[]{String.valueOf(c.getID())});
        database.close();
    }

    public void removeClass(Class c) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("Classes", "CId = " + c.getID(), null);
        database.close();
    }

    /* Exam functions */

    public ArrayList<Exam> getAllExams() {
        ArrayList<Exam> exams = new ArrayList<Exam>();
        String selectQuery = "SELECT * FROM Exams";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String room = cursor.getString(2);
                long time = cursor.getLong(3);
                int reminder = cursor.getInt(4);

                Exam exam = new Exam(time, name, room, reminder);
                exam.setID(id);
                exams.add(exam);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return exams;
    }

    public ArrayList<Exam> getExamsForDay(Calendar cal) {
        ArrayList<Exam> exams = new ArrayList<Exam>();
        int queryYear = cal.get(Calendar.YEAR);
        int queryMonth = cal.get(Calendar.MONTH);
        int queryDay = cal.get(Calendar.DAY_OF_MONTH);

        String selectQuery = "SELECT * FROM Exams";

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String room = cursor.getString(2);
                long time = cursor.getLong(3);
                int reminder = cursor.getInt(4);

                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(time);

                if (queryYear == c.get(Calendar.YEAR) &&
                        queryMonth == c.get(Calendar.MONTH) &&
                        queryDay == c.get(Calendar.DAY_OF_MONTH)) {
                    Exam exam = new Exam(time, name, room, reminder);
                    exam.setID(id);
                    exams.add(exam);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return exams;
    }

    public ArrayList<Exam> getExamsForWeek(Calendar cal) {
        ArrayList<Exam> examsForWeek = new ArrayList<Exam>();
        int add = 0;
        for (int i = 0; i < 7; i++) {
            cal.add(Calendar.DAY_OF_MONTH, add);
            examsForWeek.addAll(getExamsForDay(cal));
            add = 1;
        }
        return examsForWeek;
    }

    public void addExam(Exam exam) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("EName", exam.getName());
        cv.put("CRoom", exam.getRoom());
        cv.put("ETime", exam.getTime());
        cv.put("Reminder", exam.getReminder());

        database.insert("Exams", null, cv);

        Cursor c = database.rawQuery("SELECT last_insert_rowid() from " + EXAM_TABLE_NAME, null);

        if (c != null) {
            if (c.moveToFirst() == false) {
                c.close();
                database.close();
                return;
            }

            int id = c.getInt(0);
            exam.setID(id);
        }

        c.close();
        database.close();
    }

    public void updateExam(Exam exam) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("EName", exam.getName());
        cv.put("CRoom", exam.getRoom());
        cv.put("ETime", exam.getTime());
        cv.put("Reminder", exam.getReminder());

        database.update("Exams", cv, "EId = ?", new String[]{String.valueOf(exam.getID())});
        database.close();
    }

    public void removeExam(Exam exam) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("Exams", "EId = " + exam.getID(), null);
        database.close();
    }

    /* Exam functions */

    public ArrayList<Homework> getAllHomework() {
        Homework hw;
        ArrayList<Homework> hws = new ArrayList<Homework>();
        String selectQuery = "SELECT * FROM Homework";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String cname = cursor.getString(1);
                String hwname = cursor.getString(2);
                long time = cursor.getLong(3);
                int reminder = cursor.getInt(4);

                hw = new Homework(time, cname, hwname, reminder);
                hw.setID(id);
                hws.add(hw);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return hws;
    }

    public ArrayList<Homework> getHomeworkForDay(Calendar cal) {
        Homework hw;
        ArrayList<Homework> hws = new ArrayList<Homework>();
        int queryYear = cal.get(Calendar.YEAR);
        int queryMonth = cal.get(Calendar.MONTH);
        int queryDay = cal.get(Calendar.DAY_OF_MONTH);

        String selectQuery = "SELECT * FROM Homework";

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String cname = cursor.getString(1);
                String hwname = cursor.getString(2);
                long time = cursor.getLong(3);
                int reminder = cursor.getInt(4);

                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(time);

                if (queryYear == c.get(Calendar.YEAR) &&
                        queryMonth == c.get(Calendar.MONTH) &&
                        queryDay == c.get(Calendar.DAY_OF_MONTH)) {
                    hw = new Homework(time, cname, hwname, reminder);
                    hw.setID(id);
                    hws.add(hw);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return hws;
    }

    public ArrayList<Homework> getHomeworkForWeek(Calendar cal) {
        ArrayList<Homework> hwForWeek = new ArrayList<Homework>();
        int add = 0;
        for (int i = 0; i < 7; i++) {
            cal.add(Calendar.DAY_OF_MONTH, add);
            hwForWeek.addAll(getHomeworkForDay(cal));
            add = 1;
        }
        return hwForWeek;
    }

    public void addHomework(Homework hw) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("CName", hw.getCname());
        cv.put("HWName", hw.getHwname());
        cv.put("Due", hw.getDue());
        cv.put("Reminder", hw.getReminder());

        database.insert("Homework", null, cv);

        Cursor c = database.rawQuery("SELECT last_insert_rowid() from " + HOMEWORK_TABLE_NAME, null);

        if (c != null) {
            if (c.moveToFirst() == false) {
                c.close();
                database.close();
                return;
            }

            int id = c.getInt(0);
            hw.setID(id);
        }

        c.close();
        database.close();
    }

    public void updateHomework(Homework hw) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("CName", hw.getCname());
        cv.put("HWName", hw.getHwname());
        cv.put("Due", hw.getDue());
        cv.put("Reminder", hw.getReminder());

        database.update("Homework", cv, "HWId = ?", new String[]{String.valueOf(hw.getID())});
        database.close();
    }

    public void removeHomework(Homework hw) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("Homework", "HWId = " + hw.getID(), null);
        database.close();
    }

}
