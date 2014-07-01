package cs408.purdueclassassistant;

/**
 * Created by frickm on 2/12/14.
 */
public class Exam {

    private int id;
    private long time;
    private String name;
    private String room;
    private int reminder;

    public Exam(long time, String name, String room, int reminder) {
        this.id = -1;
        this.time = time;
        this.name = name;
        this.room = room;
        this.reminder = reminder;
    }

    // Accessor Methods
    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }

}
