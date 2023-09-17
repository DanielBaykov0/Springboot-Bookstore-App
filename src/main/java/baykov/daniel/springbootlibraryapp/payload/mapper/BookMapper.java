package baykov.daniel.springbootlibraryapp.payload.mapper;

import baykov.daniel.springbootlibraryapp.entity.Book;
import baykov.daniel.springbootlibraryapp.payload.dto.BookDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDTO entityToDto(Book book);

    List<BookDTO> entityToDto(Iterable<Book> books);

    Book dtoToEntity(BookDTO bookDTO);

    List<Book> dtoToEntity(Iterable<BookDTO> bookDTOS);
}
