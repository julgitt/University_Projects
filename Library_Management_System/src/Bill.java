import java.time.LocalDate;

public class Bill {
    private int billID;
    static int billID_counter;
    private double prizeForDay;
    private LocalDate returnDeadline;
    private Borrow borrow;

    //constructor
    public Bill(Borrow borrow,  LocalDate returnDeadline, double prize){
        this.billID = billID_counter;
        billID_counter++;
        this.borrow = borrow;
        this.returnDeadline = returnDeadline;
        this.prizeForDay = prize;
    }

    //getters
    public int getBillID(){
        return this.billID;
    }
    public double getPrize(){
        return this.prizeForDay * daysAfterDeadline();
    }
    public Borrow getBorrow(){
        return this.borrow;
    }

    public LocalDate getReturnDeadline(){
        return this.returnDeadline;
    }

    //methods
    public long daysAfterDeadline(){
        return java.time.temporal.ChronoUnit.DAYS.between(returnDeadline, LocalDate.now());
    }
}
