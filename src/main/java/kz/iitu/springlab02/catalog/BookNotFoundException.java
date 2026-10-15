package kz.iitu.springlab02.catalog;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("Book not found: " + id);
    }
}