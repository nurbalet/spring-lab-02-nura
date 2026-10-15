package kz.iitu.springlab02.web;

import jakarta.validation.constraints.*;
import kz.iitu.springlab02.catalog.Genre;
import kz.iitu.springlab02.validation.NoDigits;

public class BookForm {

    @NotBlank(message = "{book.title.required}")
    @Size(max = 120, message = "{book.title.size}")
    private String title;

    @NotBlank(message = "{book.author.required}")
    @NoDigits(message = "{book.author.nodigits}")
    private String author;

    @Min(value = 1450, message = "{book.year.range}")
    @Max(value = 2100, message = "{book.year.range}")
    private Integer year;

    @NotNull(message = "{book.genre.required}")
    private Genre genre;

    private boolean available;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Genre getGenre() { return genre; }
    public void setGenre(Genre genre) { this.genre = genre; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}