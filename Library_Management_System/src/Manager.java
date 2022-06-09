import java.time.LocalDate;
import java.util.List;

public class Manager {
    private List<Book> books;
    private List<Author> authors;
    private List<Borrow> borrows;
    private List<User> users;
    private List<Bill> bills;

    //constructors
    public Manager(){
        books = null;
        authors = null;
        borrows = null;
        users = null;
        bills = null;
    }
    public Manager(List<Book> books, List<Author> authors, List<Borrow> borrows, List<User> users, List<Bill> bills){
        this.books = books;
        this.authors = authors;
        this.borrows = borrows;
        this.users = users;
        this.bills = bills;
    }

    //getters
    public List<Book> getBooks(){
        return books;
    }
    public  List<Author>  getAuthors(){
        return authors;
    }
    public List<Borrow> getBorrows(){
        return borrows;
    }
    public List<User> getUsers(){
        return users;
    }
    public List<Bill> getBills(){
        return bills;
    }

    //______________________________________________BOOKS___________________________________________________
    public void addBook(int bookID, String title, double prize,int authorID, String name, String secondName){
        for (Book book : books) {
            if ((book.getBookID()) == bookID) {
                throw new RuntimeException("There is already book with such index in library");
            }
        }

        for (Author author : authors) {
            if ((author.getAuthorID()) == authorID) {
                Book newBook = new Book(author, title, prize);
                author.addBook(newBook);
                books.add(newBook);
                return;
            }
        }

        Author newAuthor = new Author(name, secondName);
        Book newBook = new Book(newAuthor, title, prize);
        newAuthor.addBook(newBook);
        authors.add(newAuthor);
        books.add(newBook);
    }

    public void deleteBook(Book deleteBook){
        books.remove(deleteBook);
        deleteBook.getAuthor().deleteBook(deleteBook);
        if (deleteBook.getAuthor().HowManyBooks() == 0) {
            authors.remove(deleteBook.getAuthor());
        }

        for (int i = 0; i < borrows.size(); i++){
            if (borrows.get(i).getBook().getBookID() == deleteBook.getBookID())
                borrows.remove(borrows.get(i));
        }

    }
    //__________________________________USERS___________________________________________________

    public void addUser(int userID, String name, String secondName, int phoneNumber){
        for (User user : users) {
            if ((user.getUserID()) == userID) {
                throw new RuntimeException("There is already user with such index in library");
            }
        }

        User newUser = new User(name, secondName, phoneNumber);
        users.add(newUser);

    }

    public void deleteUser(User deleteUser){
        users.remove(deleteUser);

        for (int i = 0; i < borrows.size(); i++){
            if (borrows.get(i).getUserID() == deleteUser.getUserID())
                borrows.remove(borrows.get(i));
        }

    }

    //_____________________________________________BORROWS___________________________________________________
    public void addBorrow(int borrowID, int userID, Book book, LocalDate dateOfBorrow, int days){
        for (Borrow borrow : borrows) {
            if ((borrow.getBorrowID()) == borrowID) {
                throw new RuntimeException("There is already borrow with such index in library");
            }
        }


        Borrow newBorrow = new Borrow(userID, book, dateOfBorrow, days);
        borrows.add(newBorrow);
        book.changeStatus();
    }

    public void deleteBorrow(Borrow deleteBorrow){
        borrows.remove(deleteBorrow);
        for (int i = 0; i< bills.size(); i++){
            if (bills.get(i).getBorrow() == deleteBorrow){
                bills.remove(bills.get(i));
            }
        }
    }
    public void DeadlineChecker(){
        for (Borrow borrow : borrows) {
            if (borrow.daysToDeadline() <= 0 && !borrow.getStatus()) {
                bills.add(new Bill(borrow, borrow.getReturnDeadline(), borrow.getBook().getPrize()));
                borrow.changeStatus();
            }
        }
    }
    //_____________________________________SHOW DATA____________________________________________________
    public void ShowBooksInLibrary(){
        for (Book book : books) {
            System.out.println(book);
        }
    }
    public void ShowAuthors(){
        for (Author author : authors) {
            System.out.println(author);
        }
    }
    public void ShowBorrows(){
        for (Borrow borrow : borrows) {
            System.out.println(borrow);
        }
    }
    public void ShowUsers(){
        for (User user : users) {
            System.out.println(user);
        }
    }
    public void ShowBills(){
        for (Bill bill : bills) {
            System.out.println(bill);
        }
    }
}
