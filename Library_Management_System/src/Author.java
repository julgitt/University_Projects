import java.util.List;

public class Author {
        private int authorID;
        static int authorID_counter = 0;
        private String name;
        private String secondName;
        private List<Book> booksInLibrary;


        //constructor
        public Author(String name, String secondName){
            this.authorID = authorID_counter; //change to author
            authorID_counter++;
            this.name = name;
            this.secondName = secondName;
            this.booksInLibrary = null;
        }

        //getters
        public int getAuthorID(){
            return this.authorID;
        }
        public String getName(){
            return this.name;
        }
        public String getSecondName(){
            return this.secondName;
        }


        //methods
        public void addBook(Book newBook){
            booksInLibrary.add(newBook);
        }
        public void deleteBook(Book deletedBook){
            booksInLibrary.remove(deletedBook);
        }

         public void ShowBooksInLibrary(){
            for (Book book : booksInLibrary) {
                System.out.println(book);
            }
        }
         public int HowManyBooks(){
            return booksInLibrary.size();
        }

        @Override
        public String toString(){
            return getName() + getSecondName();
        }
}
