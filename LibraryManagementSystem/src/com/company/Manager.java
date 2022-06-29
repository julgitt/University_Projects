package com.company;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Manager implements Serializable {
    private final IDSequence booksID;
    private final IDSequence authorsID;
    private final IDSequence borrowsID;
    private final IDSequence usersID;
    private final IDSequence billsID;

    private final List<Book> books;
    private final List<Author> authors;
    private final List<Borrow> borrows;
    private final List<User> users;
    private final List<Bill> bills;

    //constructors
    public Manager() {
        booksID = new IDSequence();
        authorsID = new IDSequence();
        borrowsID = new IDSequence();
        usersID = new IDSequence();
        billsID = new IDSequence();

        books = new ArrayList<Book>();
        authors = new ArrayList<Author>();
        borrows = new ArrayList<Borrow>();
        users = new ArrayList<User>();
        bills = new ArrayList<Bill>();
    }

    public Manager(List<Book> books, List<Author> authors, List<Borrow> borrows, List<User> users, List<Bill> bills, AtomicLong bookID, AtomicLong authorID,AtomicLong borrowID,AtomicLong userID,AtomicLong billID) {
        booksID = new IDSequence(bookID);
        authorsID = new IDSequence(authorID);
        borrowsID = new IDSequence(borrowID);
        usersID = new IDSequence(userID);
        billsID = new IDSequence(billID);

        this.books = books;
        this.authors = authors;
        this.borrows = borrows;
        this.users = users;
        this.bills = bills;
    }

    //getters
    public List<Book> getBooks() {
        return books;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<Borrow> getBorrows() {
        return borrows;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Bill> getBills() {
        return bills;
    }

    //______________________________________________BOOKS___________________________________________________
    public void addBook(String title, double price, String name, String secondName) {

        for (Author author : authors) { //if there is already an author in the database
            if (Objects.equals(author.getName(), name) && Objects.equals(author.getSecondName(), secondName)) {
                Book newBook = new Book(author, title, price, booksID.getNextValue());
                author.addBook(newBook);
                books.add(newBook);
                return;
            }
        }
        //otherwise we create a new one
        Author newAuthor = new Author(name, secondName, authorsID.getNextValue());
        Book newBook = new Book(newAuthor, title, price, booksID.getNextValue());
        newAuthor.addBook(newBook);

        authors.add(newAuthor);
        books.add(newBook);
    }

    public void deleteBook(long id) {
        Book deletedBook = null;
        for (Book book : books) { //delete book with the given id
            if (id == book.getBookID()) {
                deletedBook = book;
                books.remove(book);
                break;
            }
        }

        assert deletedBook != null;
        deletedBook.getAuthor().deleteBook(deletedBook);
        if (deletedBook.getAuthor().HowManyBooks() == 0) { // if author has no more books in the library
            authors.remove(deletedBook.getAuthor());       // delete author from the database
        }

    }

    //__________________________________USERS___________________________________________________

    public void addUser(String name, String secondName, int phoneNumber) {
        User newUser = new User(name, secondName, phoneNumber, usersID.getNextValue());
        users.add(newUser);
    }

    public void deleteUser(long id) {
        User deletedUser = null;
        for (User user : users) {
            if (id == user.getUserID()) {
                deletedUser = user;
                users.remove(user);
                break;
            }
        }
    }

    //_____________________________________________BORROWS___________________________________________________
    public void addBorrow(int userID, int bookID, LocalDate dateOfBorrow, int days) {
        User userBorrow = null;
        Book bookBorrow = null;
        for (User user : users) {
            if (userID == user.getUserID()) {
                userBorrow = user;
                break;
            }
        }
        for (Book book : books) {
            if (bookID == book.getBookID()) {
                bookBorrow = book;
                break;
            }
        }
        if (bookBorrow == null || userBorrow == null)
            throw new RuntimeException("There is no user or book with such index");

        Borrow newBorrow = new Borrow(userBorrow, userID, bookBorrow, bookID, dateOfBorrow, days, borrowsID.getNextValue());
        borrows.add(newBorrow);
        bookBorrow.setStatus("borrowed");
    }

    public void deleteBorrow(long id) {
        for (Borrow borrow : borrows) {
            if (id == borrow.getBorrowID()) {
                borrow.getBook().setStatus("not borrowed");
                borrows.remove(borrow);
                break;
            }
        }
    }

    public void DeadlineChecker() {
        for (Borrow borrow : borrows) {
            if ( borrow.getReturnDeadline().isBefore(ChronoLocalDate.from(LocalDateTime.now())) && !borrow.getStatus()) {
                bills.add(new Bill(borrow, borrow.getReturnDeadline(), borrow.getBook().getPrice(), billsID.getNextValue()));
                borrow.changeStatus();
            }
        }
    }

    //_______________________________________BILLS_____________________________________________________
    public void deleteBill(long id) {
        for (Bill bill : bills) {
            if (id == bill.getBillID()) {
                bills.remove(bill);
                break;
            }
        }
    }

    //_____________________________________SHOW DATA____________________________________________________
    @Override
    public String toString(){
        return toStringBooks() + " " + booksID + " " + toStringAuthors() + " " + authorsID + " " + toStringUsers()
                +  " " + usersID + " " + toStringBorrows() + " " + borrowsID + " "+ toStringBills() + billsID + " ";
    }

    public String toStringBooks() {
        String result = "";
        for (Book book : books) {
            result += book.toString() + " ";
        }
        return result;
    }

    public String toStringAuthors() {
        String result = "";
        for (Author author : authors) {
            result += author.toString() + " ";
        }
        return result;
    }

    public String toStringBorrows() {
        String result = "";
        for (Borrow borrow : borrows) {
            result += borrow.toString() + " ";
        }
        return result;
    }

    public String toStringUsers() {
        String result = "";
        for (User user : users) {
            result += user.toString() + " ";
        }
        return result;
    }

    public String toStringBills() {
        String result = "";
        for (Bill bill : bills) {
            result += bill.toString() + " ";
        }
        return result;
    }
}
