package kz.iitu.springlab02.web;

import jakarta.validation.Valid;
import kz.iitu.springlab02.catalog.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
public class BookPageController {

    private final BookService service;
    private final AuthorRepository authorRepository;

    public BookPageController(BookService service, AuthorRepository authorRepository) {
        this.service = service;
        this.authorRepository = authorRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("books", service.findAll());
        model.addAttribute("title", "Catalogue");
        return "books/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new BookForm());
        model.addAttribute("genres", Genre.values());
        return "books/form";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable long id, Model model) {
        Book book = service.getById(id);
        model.addAttribute("book", book);
        model.addAttribute("title", "Book details");
        return "books/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable long id, Model model) {
        Book book = service.getById(id);
        BookForm form = new BookForm();
        form.setId(book.getId());
        form.setTitle(book.getTitle());
        form.setAuthor(book.getAuthor() != null ? book.getAuthor().getName() : "");
        form.setYear(book.getYear());
        form.setGenre(book.getGenre());

        model.addAttribute("form", form);
        model.addAttribute("genres", Genre.values());
        return "books/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") BookForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            model.addAttribute("genres", Genre.values());
            return "books/form";
        }

        Author author = findOrCreateAuthor(form.getAuthor());
        Book book = new Book(form.getTitle(), form.getGenre(), form.getYear());
        book.setAuthor(author);

        service.create(book);
        redirect.addFlashAttribute("message", "Book " + book.getTitle() + " has been added");
        return "redirect:/books";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable long id, @Valid @ModelAttribute("form") BookForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            model.addAttribute("genres", Genre.values());
            return "books/form";
        }

        Author author = findOrCreateAuthor(form.getAuthor());
        Book book = new Book(form.getTitle(), form.getGenre(), form.getYear());
        book.setAuthor(author);

        service.update(id, book);
        redirect.addFlashAttribute("message", "Book " + form.getTitle() + " has been updated");
        return "redirect:/books";
    }

    // Вспомогательный метод для поиска или создания автора
    private Author findOrCreateAuthor(String authorName) {
        return authorRepository.findAll().stream()
                .filter(a -> a.getName().equalsIgnoreCase(authorName))
                .findFirst()
                .orElseGet(() -> authorRepository.save(new Author(authorName, "Unknown")));
    }
}