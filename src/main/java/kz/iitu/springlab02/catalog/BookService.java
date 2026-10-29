package kz.iitu.springlab02.catalog;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;

    @Autowired
    private EntityManager entityManager; // Нужен для симуляции блокировки

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll() {
        return repository.findAll();
    }

    public Page<Book> findPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    public Book getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public Book create(Book book) {
        return repository.save(book);
    }

    public Book update(Long id, Book book) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setTitle(book.getTitle());
                    existing.setAuthor(book.getAuthor());
                    existing.setGenre(book.getGenre());
                    existing.setYear(book.getYear());
                    return repository.save(existing);
                }).orElseThrow(() -> new BookNotFoundException(id));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        repository.deleteById(id);
    }

    // === ИНДИВИДУАЛЬНЫЙ ВАРИАНТ 4: СИМУЛЯЦИЯ КОНФЛИКТА ===
    @Transactional
    public void simulateLock(Long id) {
        // 1. Пользователь А открывает книгу
        Book userA = repository.findById(id).orElseThrow();
        entityManager.detach(userA); // Отключаем от Hibernate (версия 0)

        // 2. Пользователь Б открывает ту же книгу и сохраняет изменения (версия становится 1)
        Book userB = repository.findById(id).orElseThrow();
        userB.setTitle("Изменено пользователем Б");
        repository.saveAndFlush(userB);

        // 3. Пользователь А пытается сохранить свою старую версию (версия 0)
        userA.setTitle("Изменено пользователем А (конфликт!)");
        repository.saveAndFlush(userA); // ЗДЕСЬ ВЫЛЕТИТ OptimisticLockException
    }
}