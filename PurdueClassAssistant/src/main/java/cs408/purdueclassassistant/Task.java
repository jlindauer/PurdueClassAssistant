package cs408.purdueclassassistant;

/**
 * Created by tannermcrae on 11/30/13.
 */
public class Task {

    private int id;
    private long time;
    private String description;

    public Task(long time, String description) {
        this.id = -1;
        this.time = time;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

