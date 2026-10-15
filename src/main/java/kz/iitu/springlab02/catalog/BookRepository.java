package kz.iitu.springlab02.catalog;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BookRepository {

    private final Map<Long, Book> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public BookRepository() {
        save(new Book(null, "Effective Java", "Joshua Bloch", 2018, Genre.NON_FICTION));
        save(new Book(null, "Clean Code", "Robert Martin", 2008, Genre.NON_FICTION));
        save(new Book(null, "Spring in Action", "Craig Walls", 2022, Genre.NON_FICTION));
    }

    public List<Book> findAll() {
        return List.copyOf(storage.values());
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Book save(Book book) {
        long id = book.id() != null ? book.id() : sequence.incrementAndGet();
        Book stored = new Book(id, book.title(), book.author(), book.year(), book.genre());
        storage.put(id, stored);
        return stored;
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }
}