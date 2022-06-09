import java.time.LocalDate;

public class Borrow {
    private int borrowID;
    private int userID;
    static int borrowID_counter;
    private Book book;
    private boolean status; //if deadline
    private LocalDate dateOfBorrow;
    private LocalDate returnDeadline;

    //constructor
    public Borrow(int userID, Book book, LocalDate dateOfBorrow, int days){
        this.borrowID = borrowID_counter;
        borrowID_counter++;
        this.userID = userID;
        this.book = book;
        this.dateOfBorrow = dateOfBorrow;//deadline
        this.returnDeadline = dateOfBorrow.plusDays(days);
        this.status = false;
    }

    //getters
    public int getBorrowID(){
        return this.borrowID;
    }
    public int getUserID(){
        return this.userID;
    }
    public Book getBook(){
        return this.book;
    }
    public LocalDate getDateOfBorrow(){
        return this.dateOfBorrow;
    }
    public boolean getStatus(){
        return this.status;
    }
    public LocalDate getReturnDeadline(){
        return this.returnDeadline;
    }
    public void changeStatus(){
        status = ! status;
    }

    //methods
    public long daysToDeadline(){
        return java.time.temporal.ChronoUnit.DAYS.between(dateOfBorrow,returnDeadline);
    }


    @Override
    public String toString(){
        return String.valueOf(getBorrowID());
    }

}
