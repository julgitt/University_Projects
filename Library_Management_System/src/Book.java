import java.util.Objects;

public class Book {
    private int bookID;
    static int bookID_counter = 0;
    private Author author;
    private String title;
    private Boolean status; //is Borrowed?
    private double prize;


    //constructor
    public Book(Author author, String title, double prize){
        this.bookID = bookID_counter;
        bookID_counter++;
        this.author = author; //change to author
        this.title = title;
        this.prize = prize;
        this.status = false;
    }

    //getters
    public int getBookID(){
        return this.bookID;
    }
    public String getTitle(){
        return this.title;
    }
    public Author getAuthor(){
        return this.author;
    }

    public Boolean getStatus(){
        return this.status;
    }
    public double getPrize(){
        return this.prize;
    }

    //setters
    public void changeStatus(){
        this.status = !status;
    }
    public void setBookID(int ID){
        bookID = ID;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setAuthor(String author){
        String [] words = author.split(" ", 1);
        this.author = new Author(words[0], words[1]);
    }

    public void setStatus(String status){
        if (Objects.equals(status, "borrowed")){
            this.status = true;
        }
        else{
            this.status = false;
        }
    }
    public void setPrize(String prize){
        this.prize = Double.parseDouble(prize);
    }

    //methods
    public void showBookDetails(){
        System.out.println("ID: " + bookID + "\nTitle: " + title + "\nAuthor: " + getAuthor().toString() +
                "\nStatus: " + ((this.getStatus()) ? "borrowed" : "not borrowed") + "\nPrize:" + prize +"\n");
    }

    @Override
    public String toString(){
        return getTitle();
    }

}
