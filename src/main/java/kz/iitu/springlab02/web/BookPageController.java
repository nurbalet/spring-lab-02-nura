package kz.iitu.springlab02.web;

import kz.iitu.springlab02.catalog.Book;
import kz.iitu.springlab02.catalog.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @PostMapping
    public String create(
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam int year,
            RedirectAttributes redirectAttributes
    ) {
        Book created = service.create(new Book(null, title, author, year));
        redirectAttributes.addFlashAttribute("message", "Book created: " + created.title());
        return "redirect:/books";
    }
}