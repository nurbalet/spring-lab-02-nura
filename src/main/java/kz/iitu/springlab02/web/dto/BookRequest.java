package kz.iitu.springlab02.web.dto;

import jakarta.validation.constraints.*;
import kz.iitu.springlab02.catalog.Genre;

public record BookRequest(
        @NotBlank(message = "{book.title.required}")
        @Size(max = 120, message = "{book.title.size}")
        String title,

        @NotBlank(message = "{book.author.required}")
        String author,

        @Min(value = 1450, message = "{book.year.range}")
        @Max(value = 2100, message = "{book.year.range}")
        int year,

        @NotNull(message = "{book.genre.required}")
        Genre genre
) {}