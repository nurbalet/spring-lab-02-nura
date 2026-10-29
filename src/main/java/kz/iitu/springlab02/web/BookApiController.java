package kz.iitu.springlab02.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.iitu.springlab02.catalog.Book;
import kz.iitu.springlab02.catalog.BookNotFoundException;
import kz.iitu.springlab02.catalog.BookService;
import kz.iitu.springlab02.web.dto.BookMapper;
import kz.iitu.springlab02.web.dto.BookRequest;
import kz.iitu.springlab02.web.dto.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Books", description = "The catalogue of books")
@RestController
@RequestMapping("/api/books")
public class BookApiController {

    private final BookService service;
    private final BookMapper mapper;

    public BookApiController(BookService service, BookMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<BookResponse> list(
            @PageableDefault(size = 5, sort = "title") Pageable pageable) {
        return service.findPage(pageable).map(mapper::toResponse);
    }

    @Operation(summary = "Find a book by its identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The book was found"),
            @ApiResponse(responseCode = "404", description = "No book with this id",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public BookResponse find(
            @Parameter(description = "Identifier of the book", example = "42")
            @PathVariable long id) {
        return service.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Operation(summary = "Create a new book")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "The book was created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Duplicate title")
    })
    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
        Book saved = service.create(mapper.toDomain(request));
        return ResponseEntity
                .created(URI.create("/api/books/" + saved.getId()))
                .body(mapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable long id, @Valid @RequestBody BookRequest request) {
        Book updated = service.update(id, mapper.toDomain(request));
        return mapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/simulate-lock")
    public String simulateLock(@PathVariable Long id) {
        service.simulateLock(id);
        return "Lock test passed (no conflict)";
    }
}