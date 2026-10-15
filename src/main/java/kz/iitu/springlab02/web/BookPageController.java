package kz.iitu.springlab02.web;

import jakarta.validation.Valid;
import kz.iitu.springlab02.catalog.Book;
import kz.iitu.springlab02.catalog.BookService;
import kz.iitu.springlab02.catalog.Genre;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
public class BookPageController {

    private final BookService service;

    public BookPageController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public String list(
            @RequestParam(required = false) String author,
            Model model
    ) {
        model.addAttribute("books", service.findAll(author));
        model.addAttribute("author", author);
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

    @PostMapping
    public String create(
            @Valid @ModelAttribute("form") BookForm form,
            BindingResult binding,
            Model model,
            RedirectAttributes redirect
    ) {
        if (binding.hasErrors()) {
            model.addAttribute("genres", Genre.values());
            return "books/form";
        }

        Book saved = service.create(new Book(
                null,
                form.getTitle(),
                form.getAuthor(),
                form.getYear(),
                form.getGenre()
        ));

        redirect.addFlashAttribute("message", "Book " + saved.title() + " has been added");
        return "redirect:/books";
    }
}