package apps.brandon.finance;


import java.util.Comparator;
import java.util.Random;

public class BillData {

    public String name;
    public String day;
    public String description;
    public String amount;
    public String id;
//    public int billImage; IF YOU TURN THIS BACK ON, ADD IT TO THE CONSTRUCTOR

    BillData(){
        this.name = "";
        this.day = "";
        this.description = "";
        this.amount = "";
        Random rand = new Random();
        this.id = String.valueOf( rand.nextInt(1000) + 1 );    }

    BillData(String name, String day, String description, String amount){
        this.name = name;
        this.day = day;
        this.description = description;
        this.amount = amount;
        Random rand = new Random();
        this.id = String.valueOf( rand.nextInt(1000) + 1 );
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public static Comparator<BillData> BillDayComparator = new Comparator<BillData>() {
        @Override
        public int compare(BillData o1, BillData o2) {
            Integer day1 = Integer.valueOf(o1.getDay());
            Integer day2 = Integer.valueOf(o2.getDay());

            return day1.compareTo(day2);
        }};

    @Override
    public String toString() {
        return "BillData{" +
                "name='" + name + '\'' +
                ", day='" + day + '\'' +
                ", description='" + description + '\'' +
                ", amount='" + amount + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}


















