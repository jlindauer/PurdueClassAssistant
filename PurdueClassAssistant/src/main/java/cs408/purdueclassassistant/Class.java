package cs408.purdueclassassistant;

/**
 * Created by frickm on 2/12/14.
 */
public class Class {

    private int id;
    private int day;
    private long start;
    private long end;
    private String name;
    private String desc;
    private String room;
    private String teacher;
    private int reminder;

    public Class(int day, long start, long end, String name, String desc, String room, String teacher, int reminder) {
        this.id = -1;
        this.day = day;
        this.start = start;
        this.end = end;
        this.name = name;
        this.desc = desc;
        this.room = room;
        this.teacher = teacher;
        this.reminder = reminder;
    }

    // Accessor Methods
    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }

}
