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

    public Book getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public Book create(Book book) {
        boolean exists = repository.findAll().stream()
                .anyMatch(b -> b.title().equalsIgnoreCase(book.title()));
        if (exists) {
            throw new DuplicateTitleException(book.title());
        }
        return repository.save(book);
    }

    public Book update(Long id, Book book) {
        return repository.findById(id)
                .map(existing -> repository.save(
                        new Book(
                                id,
                                book.title(),
                                book.author(),
                                book.year(),
                                book.genre(),
                                existing.createdAt() // Берем дату создания из старой записи
                        )
                ))
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new BookNotFoundException(id);
        }
    }
}