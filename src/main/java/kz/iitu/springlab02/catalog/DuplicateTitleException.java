package kz.iitu.springlab02.catalog;

public class DuplicateTitleException extends RuntimeException {

    private final String title;

    public DuplicateTitleException(String title) {
        super("Book with title '" + title + "' already exists");
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}