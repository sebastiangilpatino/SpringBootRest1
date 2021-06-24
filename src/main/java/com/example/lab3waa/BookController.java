package com.example.lab3waa;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookController {

    List<Book> books = new ArrayList<Book>();

    public BookController() {
        books.add(new Book("111", "sebitas", "deport", 12.2));
        // books.add(new Book("222", "alejandrita", "rock", 620d));
    }

    @PostMapping("/book")
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        books.add(book);
        return new ResponseEntity<Book>(book, HttpStatus.OK);
    }

    @PutMapping("/book/{isbn}")
    public ResponseEntity<?> updateBook(@PathVariable String isbn, @RequestBody Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(isbn)) {
                books.set(i, book);
                break;
            }
        }
        return new ResponseEntity<Book>(book, HttpStatus.OK);
    }

    @GetMapping("/book/{isbn}")
    public ResponseEntity<?> getBook(@PathVariable String isbn) {
        Book book = null;
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(isbn)) {
                book = books.get(i);
                break;
            }
        }

        if (book == null) {
            return new ResponseEntity<CustomError>(new CustomError("Book # "
                    + isbn + " is not available"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Book>(book, HttpStatus.OK);
    }

    @DeleteMapping("/book/{isbn}")
    public ResponseEntity<?> deleteBook(@PathVariable String isbn) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(isbn)) {
                books.remove(i);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<CustomError>(new CustomError("Book # "
                + isbn + " is not available"), HttpStatus.NOT_FOUND);
    }

//    @GetMapping("/book")
//    public ResponseEntity<?> getAllBooks() {
//        Books wrapper = new Books(books);
//        return new ResponseEntity<Books>(wrapper, HttpStatus.OK);
//    }

    @GetMapping("/book")
    public ResponseEntity<?> searchBooks(@RequestParam(value = "author", required = false) String author) {
        Books wrapper = new Books();
        if (author == null) {
            wrapper.setBooks(books);
        } else {
            String authorName = author.substring(1, author.length() - 1); //remove quotes form the name
            wrapper.setBooks(books.stream()
                    .filter(x -> x.getAuthor().equals(authorName))
                    .collect(Collectors.toList()));

            if (wrapper.getBooks() == null) {
                return new ResponseEntity<CustomError>(new CustomError("Author # "
                        + author + " is not available"), HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<Books>(wrapper, HttpStatus.OK);

    }


}
