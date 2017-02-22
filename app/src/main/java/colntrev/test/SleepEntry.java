package colntrev.test;

/**
 * Created by Thy Vu on 2/20/17.
 */

public class SleepEntry {
    private long id;
    private String date;
    private double realDuration;
    private double wantedDuration;
    private String activity;

    public SleepEntry(long id, String date, double realDuration, double wantedDuration, String activity){
        this.id = id;
        this.date = date;
        this.realDuration = realDuration;
        this.wantedDuration = wantedDuration;
        this.activity = activity;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRealDuration() {
        return realDuration;
    }

    public void setRealDuration(double realDuration) {
        this.realDuration = realDuration;
    }

    public double getWantedDuration() {
        return wantedDuration;
    }

    public void setWantedDuration(double wantedDuration) {
        this.wantedDuration = wantedDuration;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
