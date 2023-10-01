package baykov.daniel.springbootlibraryapp.payload.mapper;

import baykov.daniel.springbootlibraryapp.entity.Author;
import baykov.daniel.springbootlibraryapp.payload.dto.request.AuthorRequestDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.response.AuthorResponseDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorResponseDTO entityToDTO(Author author);

    List<AuthorResponseDTO> entityToDTO(Iterable<Author> authors);

    Author dtoToEntity(AuthorRequestDTO authorRequestDTO);

    List<Author> dtoToEntity(Iterable<AuthorRequestDTO> authorDTOS);
}
