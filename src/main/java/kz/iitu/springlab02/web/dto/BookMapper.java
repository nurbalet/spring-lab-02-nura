package kz.iitu.springlab02.web.dto;

import kz.iitu.springlab02.catalog.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    Book toDomain(BookRequest request);

    BookResponse toResponse(Book book);

    List<BookResponse> toResponseList(List<Book> books);
}