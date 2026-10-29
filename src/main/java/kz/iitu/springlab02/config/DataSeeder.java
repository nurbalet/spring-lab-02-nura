package kz.iitu.springlab02.config;

import kz.iitu.springlab02.catalog.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
@Profile("!test")
public class DataSeeder implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final AccountRepository accountRepository;

    public DataSeeder(AuthorRepository authorRepository,
                      TagRepository tagRepository,
                      AccountRepository accountRepository) {
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) {
        // 1. Заполняем счета (для Лабы 9)
        if (accountRepository.count() == 0) {
            accountRepository.save(new Account("Alice", new BigDecimal("1000.00")));
            accountRepository.save(new Account("Bob", new BigDecimal("500.00")));
        }

        // 2. Заполняем книги (из Лабы 8)
        if (authorRepository.count() > 0) return;

        Tag classic = tagRepository.save(new Tag("Classic"));
        Tag programming = tagRepository.save(new Tag("Programming"));
        Tag java = tagRepository.save(new Tag("Java"));

        Author bloch = new Author("Joshua Bloch", "USA");
        Book b1 = new Book("Effective Java", Genre.NON_FICTION, 2018);
        b1.getTags().addAll(Set.of(programming, java));
        bloch.addBook(b1);

        Author martin = new Author("Robert Martin", "USA");
        Book b2 = new Book("Clean Code", Genre.NON_FICTION, 2008);
        b2.getTags().add(programming);
        martin.addBook(b2);

        Author walls = new Author("Craig Walls", "USA");
        Book b3 = new Book("Spring in Action", Genre.NON_FICTION, 2022);
        b3.getTags().add(java);
        walls.addBook(b3);

        authorRepository.save(bloch);
        authorRepository.save(martin);
        authorRepository.save(walls);
    }
}