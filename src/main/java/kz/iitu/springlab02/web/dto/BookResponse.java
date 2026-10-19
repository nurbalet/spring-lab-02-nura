package kz.iitu.springlab02.web.dto;

import kz.iitu.springlab02.catalog.Genre;
import java.time.Instant;

public record BookResponse(
        Long id,
        String title,
        String author,
        int year,
        Genre genre,
        Instant createdAt
) {}