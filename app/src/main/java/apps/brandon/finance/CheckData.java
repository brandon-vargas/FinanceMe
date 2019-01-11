package apps.brandon.finance;

public class CheckData {

    public String date;
    public int used;
    public int id;

    public CheckData(String date, int used, int id) {
        this.date = date;
        this.used = used;
        this.id = id;
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

    public void setId(int id) {this.id = id;}

    public int getId() {return id;}

    @Override
    public String toString(){

        return(this.date + " "
                + Integer.toString(this.used)
                + Integer.toString(this.id));

    }
}
