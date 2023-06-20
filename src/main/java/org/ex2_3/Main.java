package org.ex2_3;

import org.ex2_3.entity.Author;
import org.ex2_3.entity.Book;
import org.ex2_3.utils.AuthorHelper;
import org.ex2_3.utils.BookHelper;

import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {

        AuthorHelper authorHelper = new AuthorHelper();
        Author author = new Author("Shevchenko");
        authorHelper.addAuthor(author);
        author = new Author("Andrii", "Franko");
        authorHelper.addAuthor(author);

        author.setName("Ivan");
        authorHelper.updateAuthorById(author);

        for (Author auth : authorHelper.getAuthorList()) {
            System.out.println(auth);
        }

        author = authorHelper.getAuthorById(1);
        if (Objects.nonNull(author)){
            System.out.println(author);
        }

        authorHelper.updateByLastNameBigSeven();

        List<Author> listAuthor = authorHelper.getAuthorList();
        for (Author auth : listAuthor) {
            System.out.println(auth);
        }

        BookHelper bookHelper = new BookHelper();
        bookHelper.addBook(new Book("Franko", listAuthor.get(1)));
        bookHelper.addBook(new Book("Volu", listAuthor.get(0)));

        for (Book book : bookHelper.getBookList()) {
            System.out.println(book);
        }
        Book book = bookHelper.getBookById(4);

        if (Objects.nonNull(book)) {
            System.out.println(book);
            book.setTitle("NewTitle");
            bookHelper.updateBookById(book);
        }

        bookHelper.delBookById(1);
        bookHelper.delBookByAuthor("Shevchenko");

        for (Book bookRef : bookHelper.getBookExpression("%ra%")) {
            System.out.println(bookRef);
        }

        if (false) {
            authorHelper.delAuthor("Shevchenko");
            authorHelper.flush();
        }
    }
}