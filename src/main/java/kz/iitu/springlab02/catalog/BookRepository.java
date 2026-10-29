package kz.iitu.springlab02.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByGenre(Genre genre);
    List<Book> findByTitleContainingIgnoreCase(String fragment);
    List<Book> findByYearBetweenOrderByYearDesc(int from, int to);
    List<Book> findTop3ByGenreOrderByYearDesc(Genre genre);
    long countByAuthorName(String name);

    @Query("select b from Book b where b.genre = :genre and b.year = :year order by b.title")
    List<Book> search(@Param("genre") Genre genre, @Param("year") int year);

    @Modifying(clearAutomatically = true)
    @Query("update Book b set b.archived = true where b.year < :year")
    int archiveOlderThan(@Param("year") int year);

    @Query("select b from Book b join fetch b.author")
    List<Book> findAllWithAuthor();

    @Override
    @EntityGraph(attributePaths = {"author"})
    Page<Book> findAll(Pageable pageable);
}