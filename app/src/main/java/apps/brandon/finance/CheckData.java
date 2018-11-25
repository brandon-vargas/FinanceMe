package apps.brandon.finance;

public class CheckData {

    public String date;
    public int used;

    public CheckData(String date, int used) {
        this.date = date;
        this.used = used;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int isUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    @Override
    public String toString(){

        return(this.date + " "
                + Integer.toString(this.used));

    }
}
