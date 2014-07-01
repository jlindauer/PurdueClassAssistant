package cs408.purdueclassassistant;

/**
 * Created by frickm on 2/12/14.
 */
public class Homework {

    private int id;
    private long due;
    private String cname;
    private String hwname;
    private int reminder;

    public Homework(long due, String cname, String hwname, int reminder) {
        this.id = -1;
        this.due = due;
        this.cname = cname;
        this.hwname = hwname;
        this.reminder = reminder;
    }

    // Accessor Methods
    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public long getDue() {
        return due;
    }

    public void setDue(long time) {
        this.due = due;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getHwname() {
        return hwname;
    }

    public void setHwname(String hwname) {
        this.hwname = hwname;
    }

    public int getReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }

}
