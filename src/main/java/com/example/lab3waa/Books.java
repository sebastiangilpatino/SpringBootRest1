package com.example.lab3waa;

import java.util.List;

public class Books {
    private List<Book> books;

    public Books(List<Book> books) {
        this.books = books;
    }

    public Books() {
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
