package org.ex2_3.utils;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.ex2_3.entity.Author;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Objects;

import static org.ex2_3.utils.LogUtil.logException;
import static org.ex2_3.utils.LogUtil.logInfo;

public class AuthorHelper {

    private static final String CLASSNAME = AuthorHelper.class.getName();
    private SessionFactory sessionFactory;

    public AuthorHelper() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // !!! HOMEWORK 6. Exercise 2. !!!
    public void updateByLastNameBigSeven() {
        logInfo("Updating all with last name lenght > 7", CLASSNAME);
        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaUpdate<Author> cbUpdateAuthor = cb.createCriteriaUpdate(Author.class);
            Root<Author> root = cbUpdateAuthor.from(Author.class);
            cbUpdateAuthor.set(root.get("name"), "1");
            cbUpdateAuthor.where(cb.greaterThan(root.get("lastNameLength"), 7));

            session.beginTransaction();
            int result = session.createQuery(cbUpdateAuthor).executeUpdate();
            session.getTransaction().commit();
            System.out.println("AAAA");

            logInfo(result + " record(s) updated", CLASSNAME);
        } catch (Exception e) {
            logException(e, CLASSNAME);
        }
    }

    public List<Author> getAuthorList() {
        logInfo("Selecting all authors", CLASSNAME);
        List<Author> authorList = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Author> cq = cb.createQuery(Author.class);

            Root<Author> root = cq.from(Author.class);
            cq.select(root);

            Query query = session.createQuery(cq);
            authorList = query.getResultList();
        } catch (Exception e) {
            logException(e, CLASSNAME);
        }
        return authorList;
    }

    public void addAuthor(Author author) {
        logInfo("Adding " + author.getLastName(), CLASSNAME);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(author);
            session.getTransaction().commit();
            logInfo(author.getLastName() + " added", CLASSNAME);
        } catch (Exception e) {
            logException(e, CLASSNAME);
        }
    }

    public void updateAuthorById(Author author) {
        try (Session session = sessionFactory.openSession()) {
            logInfo("Updating author with id = " + author.getId(), CLASSNAME);
            session.beginTransaction();
            session.merge(author);
            session.getTransaction().commit();
            logInfo(author.getLastName() + " with id " + author.getId() + " updated", CLASSNAME);
        } catch (Exception e) {
            logException(e, CLASSNAME);
        }
    }

    public void delAuthor(String lastName) {
        try (Session session = sessionFactory.openSession()) {
            int deleted;
            logInfo("Deleting author " + lastName, CLASSNAME);
            CriteriaDelete<Author> cd = session.getCriteriaBuilder().createCriteriaDelete(Author.class);
            Root<Author> root = cd.from(Author.class);
            cd.where(root.get("lastName").in(lastName));
            session.beginTransaction();
            deleted = session.createQuery(cd).executeUpdate();
            session.getTransaction().commit();
            logInfo("Deleted " + deleted + " record(s)", CLASSNAME);
        } catch (Exception e) {
            logException(e, CLASSNAME);
        }
    }

    public Author getAuthorById(long id) {
        Author author = null;
        try (Session session = sessionFactory.openSession()) {
            logInfo("Selecting author with id = " + id, CLASSNAME);
            author = session.get(Author.class, id);
            if (Objects.isNull(author)) {
                logInfo("None found", CLASSNAME);
            }
        } catch (Exception e) {
            logException(e, CLASSNAME);
        }
        return author;
    }

    public void flush() {
        logInfo("Exercise 6 - flush", CLASSNAME);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (int i = 0; i < 200; i++) {
                Author author = new Author("New" + i, "Author" + i);
                session.persist(author);
                if ((i + 1) % 10 == 0) {
                    logInfo("flush + " + i, CLASSNAME);
                    session.flush();
                }
            }
            session.getTransaction().commit();
            logInfo("200 authors added", CLASSNAME);
        } catch (Exception e) {
            logException(e, CLASSNAME);
        }
    }
}
