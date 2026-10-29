package kz.iitu.springlab02.web.dto;

import kz.iitu.springlab02.catalog.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "archived", ignore = true)
    @Mapping(target = "version", ignore = true)
    Book toDomain(BookRequest request);

    @Mapping(target = "author", source = "author.name")
    BookResponse toResponse(Book book);

    List<BookResponse> toResponseList(List<Book> books);
}