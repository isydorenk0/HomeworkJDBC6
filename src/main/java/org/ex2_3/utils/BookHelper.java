package org.ex2_3.utils;

import jakarta.persistence.criteria.*;
import org.ex2_3.entity.Author;
import org.ex2_3.entity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Objects;

import static org.ex2_3.utils.LogUtil.logException;
import static org.ex2_3.utils.LogUtil.logInfo;


public class BookHelper {
    private SessionFactory sessionFactory;
    private static final String CLASSNAME = BookHelper.class.getName();

    public BookHelper() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public Book getBookById(long id) {
        Book book = null;
        try (Session session = sessionFactory.openSession()) {
            logInfo("Selecting book with id = " + id, CLASSNAME);
            book = session.get(Book.class, id);
            if (Objects.isNull(book)) {
                logInfo("None found", CLASSNAME);
            }
        } catch (Exception e) {
            logException(e, CLASSNAME);
        }
        return book;
    }

    public void addBook(Book book) {
        logInfo("Adding " + book.getTitle(), CLASSNAME);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(book);
            session.getTransaction().commit();
            logInfo(book.getTitle() + " added", CLASSNAME);
        } catch (Exception e) {
            logException(e, CLASSNAME);
        }
    }

    public void updateBookById(Book book) {
        try (Session session = sessionFactory.openSession()) {
            logInfo("Updating book with id = " + book.getId(), CLASSNAME);
            session.beginTransaction();
            session.merge(book);
            session.getTransaction().commit();
        } catch (Exception e) {
            logException(e, CLASSNAME);
        }
    }

    public List<Book> getBookList() {
        logInfo("Selecting all books", CLASSNAME);
        List<Book> bookList = null;
        try (Session session = sessionFactory.openSession()) {
            Query<Book> q = session.createQuery("from org.ex2_3.entity.Book", Book.class);
            bookList = q.list();
        } catch (Exception e) {
            logException(e, CLASSNAME);
        }
        return bookList;
    }

    // !!! HOMEWORK 6. Exercise 3. !!!
    public void delBookById(long value) {
        logInfo("Deleting book with id " + value, CLASSNAME);
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaDelete<Book> cbDeleteBook = cb.createCriteriaDelete(Book.class);
            Root<Book> root = cbDeleteBook.from(Book.class);
            cbDeleteBook.where(cb.equal(root.get("id"), value));
            session.beginTransaction();
            int result = session.createQuery(cbDeleteBook).executeUpdate();
            session.getTransaction().commit();
            logInfo(result + " record deleted", CLASSNAME);
        } catch (Exception e) {
            logException(e, CLASSNAME);
        }
    }

    public void delBookByAuthor(String value) {
        logInfo("Deleting book with author " + value, CLASSNAME);
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();

            Subquery<Author> subQuery = cb.createQuery().subquery(Author.class);
            Root<Author> rootAuthor = subQuery.from(Author.class);
            subQuery.select(rootAuthor.get("id")).where(cb.equal(rootAuthor.get("lastName"), value));

            CriteriaDelete<Book> cbDeleteBook = cb.createCriteriaDelete(Book.class);
            Root<Book> rootBook = cbDeleteBook.from(Book.class);
            cbDeleteBook.where(rootBook.get("author").in(subQuery));

            session.beginTransaction();
            int result = session.createQuery(cbDeleteBook).executeUpdate();
            session.getTransaction().commit();

            logInfo(result + " record(s) deleted", CLASSNAME);
        } catch (Exception e) {
            logException(e, CLASSNAME);
        }
    }

    // !!! HOMEWORK 6. Exercise 5. !!!
    public List<Book> getBookExpression(String regex) {
        List<Book> bookList = null;
        try (Session session = sessionFactory.openSession()) {
            logInfo("Selecting book by expression on title: " + regex, CLASSNAME);

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Book> cq = cb.createQuery(Book.class);

            Root<Book> rootBook = cq.from(Book.class);
            cq.select(rootBook).where(cb.like(rootBook.get("title"), regex));
            Query<Book> query = session.createQuery(cq);
            bookList = query.list();
            if (bookList.isEmpty()) {
                logInfo("None found", CLASSNAME);
            }
        } catch (Exception e) {
            logException(e, CLASSNAME);
        }
        return bookList;
    }

}
