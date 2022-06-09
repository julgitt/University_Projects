import java.util.List;

public class User {
    private int userID;
    static int userID_counter;
    private String name;
    private String secondName;
    private int phoneNumber;

    //constructor
    public User(String name, String secondName, int phoneNumber){
        this.userID = userID_counter; //change to author
        userID_counter++;
        this.name = name;
        this.secondName = secondName;
        this.phoneNumber = phoneNumber;
    }

    //getters
    public int getUserID(){
        return this.userID;
    }
    public String getName(){
        return this.name;
    }
    public String getSecondName(){
        return this.secondName;
    }
    public int getPhoneNumber(){
        return this.phoneNumber;
    }
    //methods
    public void payBill(){

    }

    @Override
    public String toString(){
        return getName() + getSecondName();
    }
}
