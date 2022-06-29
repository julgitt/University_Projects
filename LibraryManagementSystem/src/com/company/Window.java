package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Window {
    public JPanel mainPanel;
    private JButton usersButton;
    private JButton booksButton;
    private JButton authorsButton;
    private JPanel parentPanel;
    private JPanel booksCard;
    private JPanel authorsCard;
    private JPanel usersCard;
    private JTable books;
    private JButton bookAddButton;
    private JButton bookDeleteButton;
    private JTextField authorNameTextField;
    private JTextField authorSecondNameTextField;
    private JTextField titleTextField;
    private JTextField priceTextField;
    private JPanel booksTable;
    private JPanel bookNewLabel;
    private JPanel bookButtons;
    private JPanel bookTextFields;
    private JTextField nameTextField;
    private JTextField secondNameTextField;
    private JTextField phoneTextField;
    private JPanel userTextFields;
    private JButton userDeleteButton;
    private JButton userAddButton;
    private JPanel userButtons;
    private JPanel userNewLabel;
    private JTable users;
    private JPanel usersTable;
    private JTable authors;
    private JPanel AuthorsJPanel;
    private JPanel cardButtons;
    private JScrollPane scrollUsers;
    private JScrollPane scrollAuthors;
    private JScrollPane scrollBooks;
    private JPanel savePanel;
    private JButton saveButton;
    private JButton borrowsButton;
    private JButton billsButton;
    private JPanel borrowsCard;
    private JPanel borrowsNewLabel;
    private JPanel borrowButtons;
    private JPanel borrowTextFields;
    private JPanel borrowsTable;
    private JTable borrows;
    private JScrollPane scrollBorrows;
    private JButton borrowAddButton;
    private JButton borrowDeleteButton;
    private JTextField userIDTextField;
    private JTextField bookIDTextField;
    private JTextField dateOfBorrowTextField;
    private JTextArea errors;
    private JPanel billsCard;
    private JPanel billsButtons;
    private JPanel billsTable;
    private JScrollPane scrollBills;
    private JTable bills;
    private JButton billDeleteButton;
    private JTextField numberTextField;
    private final Manager manager;

    private final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

    public Window(Manager manager) {
        this.manager = manager;

        CreateCardButtonsListeners();

        CreateBookTableEditButtonsListeners();
        CreateBorrowTableEditButtonsListeners();
        CreateUserTableEditButtonsListeners();
        CreateBillTableEditButtonsListeners();
        SaveButtonListener();

        CreateUsersTable();
        CreateBooksTable();
        CreateAuthorsTable();
        CreateBorrowsTable();
        CreateBillsTable();
        CreateUsersTable();

        errors.setText("");
    }

    //methods

    //generating buttons listeners
    private void SaveButtonListener(){
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileOutputStream file = new FileOutputStream("save.ser");
                    ObjectOutputStream oos = new ObjectOutputStream(file);
                    oos.writeObject(manager);
                    oos.close();
                    file.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
    }
    private void CreateCardButtonsListeners() {
        booksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentPanel.removeAll();
                parentPanel.add(booksCard);
                parentPanel.repaint();
                parentPanel.revalidate();
            }
        });
        authorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentPanel.removeAll();
                parentPanel.add(authorsCard);
                parentPanel.repaint();
                parentPanel.revalidate();
            }
        });
        usersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentPanel.removeAll();
                parentPanel.add(usersCard);
                parentPanel.repaint();
                parentPanel.revalidate();
            }
        });
        borrowsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentPanel.removeAll();
                parentPanel.add(borrowsCard);
                parentPanel.repaint();
                parentPanel.revalidate();
            }
        });
        billsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentPanel.removeAll();
                parentPanel.add(billsCard);
                parentPanel.repaint();
                parentPanel.revalidate();
            }
        });
    }

    private void CreateBookTableEditButtonsListeners(){
        //add book
        bookAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(titleTextField.getText(), "") || Objects.equals(priceTextField.getText(), "")
                        || Objects.equals(authorNameTextField.getText(), "") || Objects.equals(authorSecondNameTextField.getText(), "") || Objects.equals(numberTextField.getText(), ""))
                    errors.setText("You shouldn't left blank labels");
                else {
                    try {
                        errors.setText("");

                        for (long i = Long.parseLong(numberTextField.getText()); i > 0; i--) {
                            manager.addBook(titleTextField.getText(), Double.parseDouble(priceTextField.getText()), authorNameTextField.getText(), authorSecondNameTextField.getText());
                        }
                        CreateBooksTable();
                        CreateAuthorsTable();
                    }catch (NumberFormatException exc)
                    {
                        errors.setText("The price or number is not in the proper format");
                    }
                    catch (Exception exc)
                    {
                        errors.setText(exc.toString());
                    }
                }
            }
        });
        bookDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel table = (DefaultTableModel) books.getModel();
                if (books.getSelectedRow() == -1) {
                    if (books.getRowCount() == 0)
                        errors.setText("Table is Empty");
                    else
                        errors.setText("You must select a row to delete");
                } else {
                    errors.setText("");
                    long id = (long) table.getValueAt(books.getSelectedRow(), 0); //to take book ID
                    if (existsInTable(borrows, String.valueOf(id),0)){
                        errors.setText("First, delete the borrow associated with this book");
                        return;
                    }
                    manager.deleteBook(id);
                    CreateBooksTable();
                    CreateAuthorsTable();
                }
            }
        });
    }

    private void CreateUserTableEditButtonsListeners(){

        userAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(nameTextField.getText(), "") || Objects.equals(secondNameTextField.getText(), "")
                        || Objects.equals(phoneTextField.getText(), ""))
                    errors.setText("You shouldn't left blank labels");
                else {
                    try {
                        errors.setText("");
                        manager.addUser(nameTextField.getText(), secondNameTextField.getText(), Integer.parseInt(phoneTextField.getText()));
                        CreateUsersTable();
                    }
                    catch (NumberFormatException exc) {
                        errors.setText("The phone number is not in the proper format");
                    }catch (Exception exc) {
                        errors.setText(exc.toString());
                    }
                }
            }
        });

        userDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel table = (DefaultTableModel) users.getModel();
                if (users.getSelectedRow() == -1) {
                    if (users.getRowCount() == 0)
                        errors.setText("Table is Empty");
                    else
                        errors.setText("You must select a row to delete");
                } else {
                    errors.setText("");
                    long id = (long) table.getValueAt(users.getSelectedRow(), 0); //to take user ID

                    if (existsInTable(borrows, String.valueOf(id),1)){
                        errors.setText("First, delete the borrow associated with this user");
                        return;
                    }
                    manager.deleteUser(id);
                    CreateUsersTable();
                }
            }
        });
    }

    private void CreateBorrowTableEditButtonsListeners(){
        borrowAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(userIDTextField.getText(), "") || Objects.equals(bookIDTextField.getText(), "")
                        || Objects.equals(dateOfBorrowTextField.getText(), ""))
                    errors.setText("You shouldn't left blank labels");
                else {
                    try {
                        errors.setText("");
                        if (LocalDate.parse(dateOfBorrowTextField.getText()).isAfter(ChronoLocalDate.from(LocalDateTime.now()))){
                            errors.setText("You cannot set a date that is later than today");
                            return;
                        }
                        if (existsInTable(borrows,bookIDTextField.getText(),3)){
                            errors.setText("This book is already borrowed");
                            return;
                        }
                        manager.addBorrow(Integer.parseInt(userIDTextField.getText()), Integer.parseInt(bookIDTextField.getText()), LocalDate.parse(dateOfBorrowTextField.getText()), 30);

                        CreateBorrowsTable();
                        CreateBillsTable();
                        CreateBooksTable();
                    } catch (DateTimeParseException exc){
                        errors.setText("Date must be in 'yyyy-mm-dd' format");
                    }
                    catch (RuntimeException exc){
                        errors.setText("There is no user or book with such index");
                    }
                    catch (Exception exc){
                        errors.setText(exc.toString());
                    }

                }
            }
        });
        borrowDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel table = (DefaultTableModel) borrows.getModel();
                if (borrows.getSelectedRow() == -1) {
                    if (borrows.getRowCount() == 0)
                        errors.setText("Table is Empty");
                    else
                        errors.setText("You must select a row to delete");
                } else {
                    errors.setText("");
                    long id = (long) table.getValueAt(borrows.getSelectedRow(), 0);
                    manager.deleteBorrow(id);
                    CreateBorrowsTable();
                    CreateBillsTable();
                    CreateBooksTable();
                }
            }
        });
    }

    private void CreateBillTableEditButtonsListeners(){

        billDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel table = (DefaultTableModel) bills.getModel();
                if (bills.getSelectedRow() == -1) {
                    if (bills.getRowCount() == 0)
                        errors.setText("Table is Empty");
                    else
                        errors.setText("You must select a row to delete");
                } else {
                    errors.setText("");
                    if (existsInTable(borrows, String.valueOf(table.getValueAt(bills.getSelectedRow(),3)),3)) {
                        errors.setText("Delete the borrow first, and then remove the bill");
                        return;
                    }
                    long id = (long) table.getValueAt(bills.getSelectedRow(), 0);
                    manager.deleteBill(id);
                    CreateBorrowsTable();
                    CreateBillsTable();
                    CreateBooksTable();
                }
            }
        });
    }


    //generating tables
    private void CreateBooksTable() {
        int size = 0;
        if (manager.getBooks() != null) size = manager.getBooks().size();

        Object[][] data = new Object[size][5];
        for (int i = 0; i < size; i++) {
            data[i][0] = manager.getBooks().get(i).getBookID();
            data[i][1] = manager.getBooks().get(i).getTitle();
            data[i][2] = manager.getBooks().get(i).getAuthor().getName() + " " + manager.getBooks().get(i).getAuthor().getSecondName();
            data[i][3] = manager.getBooks().get(i).getPrice();
            data[i][4] = (manager.getBooks().get(i).getStatus()) ? "borrowed" : "not borrowed";
        }
        books.setModel(new DefaultTableModel(data, new String[]{"ID", "Title", "Author", "Price", "Status"}));


        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        scrollBooks.getViewport().setBackground(new Color(63, 63, 65));

        TableColumnModel columns = books.getColumnModel();
        columns.getColumn(0).setCellRenderer(centerRenderer);
        columns.getColumn(1).setCellRenderer(centerRenderer);
        columns.getColumn(2).setCellRenderer(centerRenderer);
        columns.getColumn(3).setCellRenderer(centerRenderer);
        columns.getColumn(4).setCellRenderer(centerRenderer);
    }

    private void CreateBorrowsTable() {
        int size = 0;
        if (manager.getBorrows() != null) size = manager.getBorrows().size();

        Object[][] data = new Object[size][8];
        for (int i = 0; i < size; i++) {
            data[i][0] = manager.getBorrows().get(i).getBorrowID();
            data[i][1] = manager.getBorrows().get(i).getUserID();
            data[i][2] = manager.getBorrows().get(i).getUser().getName() + " " + manager.getBorrows().get(i).getUser().getSecondName();
            data[i][3] = manager.getBorrows().get(i).getBookID();
            data[i][4] = manager.getBorrows().get(i).getBook().getTitle();
            data[i][5] = manager.getBorrows().get(i).getDateOfBorrow();
            data[i][6] = manager.getBorrows().get(i).getReturnDeadline();
            data[i][7] = (manager.getBorrows().get(i).getReturnDeadline().isBefore(ChronoLocalDate.from(LocalDateTime.now()))) ? "after the deadline" : "before the deadline";
        }

        borrows.setModel(new DefaultTableModel(data, new String[]{"ID", "UserID", "Username", "BookID", "Book's Title", "Date of borrow", "Return Date", "Status"}));


        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        scrollBorrows.getViewport().setBackground(new Color(63, 63, 65));

        TableColumnModel columns = borrows.getColumnModel();
        columns.getColumn(0).setCellRenderer(centerRenderer);
        columns.getColumn(1).setCellRenderer(centerRenderer);
        columns.getColumn(2).setCellRenderer(centerRenderer);
        columns.getColumn(3).setCellRenderer(centerRenderer);
        columns.getColumn(4).setCellRenderer(centerRenderer);
        columns.getColumn(5).setCellRenderer(centerRenderer);
        columns.getColumn(6).setCellRenderer(centerRenderer);
        columns.getColumn(7).setCellRenderer(centerRenderer);
    }

    private void CreateBillsTable() {
        manager.DeadlineChecker();
        int size = 0;
        if (manager.getBills() != null) size = manager.getBills().size();

        Object[][] data = new Object[size][7];
        for (int i = 0; i < size; i++) {
            data[i][0] = manager.getBills().get(i).getBillID();
            data[i][1] = manager.getBills().get(i).getBorrow().getUserID();
            data[i][2] = manager.getBills().get(i).getBorrow().getUser().getName() + " " + manager.getBills().get(i).getBorrow().getUser().getSecondName();
            data[i][3] = manager.getBills().get(i).getBorrow().getBookID();
            data[i][4] = manager.getBills().get(i).getBorrow().getBook().getTitle();
            data[i][5] = manager.getBills().get(i).getBorrow().getReturnDeadline();
            data[i][6] =  String.format("%.2f",(manager.getBills().get(i).getPrice()));
        }
        bills.setModel(new DefaultTableModel(data, new String[]{"ID", "UserID", "Username", "BookID", "Book Title", "Return Deadline", "Price"}));


        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        scrollBills.getViewport().setBackground(new Color(63, 63, 65));

        TableColumnModel columns = bills.getColumnModel();
        columns.getColumn(0).setCellRenderer(centerRenderer);
        columns.getColumn(1).setCellRenderer(centerRenderer);
        columns.getColumn(2).setCellRenderer(centerRenderer);
        columns.getColumn(3).setCellRenderer(centerRenderer);
        columns.getColumn(4).setCellRenderer(centerRenderer);
        columns.getColumn(5).setCellRenderer(centerRenderer);
        columns.getColumn(6).setCellRenderer(centerRenderer);
    }

    private void CreateAuthorsTable() {
        int size = 0;
        if (manager.getAuthors() != null) size = manager.getAuthors().size();

        Object[][] data = new Object[size][3];
        for (int i = 0; i < size; i++) {
            data[i][0] = manager.getAuthors().get(i).getAuthorID();
            data[i][1] = manager.getAuthors().get(i).getName();
            data[i][2] = manager.getAuthors().get(i).getSecondName();
        }

        authors.setModel(new DefaultTableModel(data, new String[]{"ID", "Name", "Second Name"}));

        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        scrollAuthors.getViewport().setBackground(new Color(63, 63, 65));

        TableColumnModel columns = authors.getColumnModel();
        columns.getColumn(0).setCellRenderer(centerRenderer);
        columns.getColumn(1).setCellRenderer(centerRenderer);
        columns.getColumn(2).setCellRenderer(centerRenderer);
    }

    private void CreateUsersTable() {
        int size = 0;
        if (manager.getUsers() != null) size = manager.getUsers().size();

        Object[][] data = new Object[size][4];
        for (int i = 0; i < size; i++) {
            data[i][0] = manager.getUsers().get(i).getUserID();
            data[i][1] = manager.getUsers().get(i).getName();
            data[i][2] = manager.getUsers().get(i).getSecondName();
            data[i][3] = manager.getUsers().get(i).getPhoneNumber();
        }
        users.setModel(new DefaultTableModel(data, new String[]{"ID", "Name", "Second Name", "Phone Number"}));

        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        scrollUsers.getViewport().setBackground(new Color(63, 63, 65));

        TableColumnModel columns = users.getColumnModel();
        columns.getColumn(0).setCellRenderer(centerRenderer);
        columns.getColumn(1).setCellRenderer(centerRenderer);
        columns.getColumn(2).setCellRenderer(centerRenderer);
        columns.getColumn(3).setCellRenderer(centerRenderer);
    }

    public boolean existsInTable(JTable table, String entry, int column) {
        // Get row and column count
        int rowCount = table.getRowCount();

        // Check against all entries
        for (int i = 0; i < rowCount; i++) {
            if (Objects.equals(table.getValueAt(i, column).toString(), entry)) {
                return true;
            }
        }
        return false;
    }

}
