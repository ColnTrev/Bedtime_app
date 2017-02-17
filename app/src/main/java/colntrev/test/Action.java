package colntrev.test;

/**
 * Created by colntrev on 2/13/17.
 */

public class Action {
    int count;
    String activity;
    boolean positive;

    public Action(){
        //empty
    }

    public Action(int num, String act, boolean experience){
        setActivity(act);
        setCount(num);
        setPositive(experience);
    }

    public void setCount(int num){
        this.count = num;
    }

    public void setActivity(String act){
        this.activity = act;
    }

    public void setPositive(boolean exp){
        this.positive = exp;
    }

    public int getCount(){
        return this.count;
    }

    public String getActivity(){
        return this.activity;
    }

    public boolean getPositive(){
        return positive;
    }
}
