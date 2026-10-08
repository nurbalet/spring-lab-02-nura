package kz.iitu.springlab02.catalog;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll(String author) {
        if (author == null || author.isBlank()) {
            return repository.findAll();
        }
        String needle = author.toLowerCase();
        return repository.findAll().stream()
                .filter(b -> b.author().toLowerCase().contains(needle))
                .toList();
    }

    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    public Book create(Book book) {
        return repository.save(new Book(null, book.title(), book.author(), book.year()));
    }

    public Optional<Book> replace(Long id, Book book) {
        return repository.findById(id)
                .map(existing -> repository.save(
                        new Book(id, book.title(), book.author(), book.year())
                ));
    }

    public boolean delete(Long id) {
        return repository.deleteById(id);
    }
}