package org.ex2_3.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicUpdate
@DynamicInsert
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Formula(" length(last_name) ")
    private int lastNameLength;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = Book.class, mappedBy = "author")
    private List<Book> books = new ArrayList<>();

    public Author(String lastName) {
        this.lastName = lastName;
    }

    public Author(String name, String lastName) {
        this(lastName);
        this.name = name;
    }

    public Author() {

    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}