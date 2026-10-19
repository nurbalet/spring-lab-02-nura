package kz.iitu.springlab02.catalog;

import java.time.Instant;

public record Book(
        Long id,
        String title,
        String author,
        int year,
        Genre genre,
        Instant createdAt
) {
}