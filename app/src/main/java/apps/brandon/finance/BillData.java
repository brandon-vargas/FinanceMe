package apps.brandon.finance;

public class BillData {

    public String name;
    public String day;
    public String description;
    public String amount;
//    public int billImage; IF YOU TURN THIS BACK ON, ADD IT TO THE CONSTRUCTOR

    BillData(){
        this.name = "";
        this.day = "";
        this.description = "";
        this.amount = "";
    }

    BillData(String name, String day, String description, String amount){
        this.name = name;
        this.day = day;
        this.description = description;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
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
}


















