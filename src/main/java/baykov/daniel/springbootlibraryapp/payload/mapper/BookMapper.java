package baykov.daniel.springbootlibraryapp.payload.mapper;

import baykov.daniel.springbootlibraryapp.entity.Book;
import baykov.daniel.springbootlibraryapp.entity.Category;
import baykov.daniel.springbootlibraryapp.payload.dto.request.BookRequestDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.response.BookResponseDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "authorNames", expression = "java(mapAuthorsNames(book))")
    @Mapping(target = "categoryNames", expression = "java(mapCategoriesNames(book))")
    BookResponseDTO entityToDTO(Book book);

    List<BookResponseDTO> entityToDTO(Iterable<Book> books);

    Book dtoToEntity(BookRequestDTO bookRequestDTO);

    List<Book> dtoToEntity(Iterable<BookRequestDTO> bookDTOS);

    default List<String> mapAuthorsNames(Book book) {
        return book.getAuthors().stream()
                .map(author -> author.getFirstName() + " " + author.getLastName())
                .collect(Collectors.toList());
    }

    default List<String> mapCategoriesNames(Book book) {
        return book.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }
}
