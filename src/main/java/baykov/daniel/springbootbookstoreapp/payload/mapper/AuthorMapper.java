package baykov.daniel.springbootbookstoreapp.payload.mapper;

import baykov.daniel.springbootbookstoreapp.entity.Author;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.AuthorRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.AuthorResponseDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    @Mapping(target = "countryId", expression = "java(author.getCountry().getId())")
    @Mapping(target = "cityId", expression = "java(author.getCity().getId())")
    AuthorResponseDTO entityToDTO(Author author);

    List<AuthorResponseDTO> entityToDTO(Iterable<Author> authors);

    Author dtoToEntity(AuthorRequestDTO authorRequestDTO);

    List<Author> dtoToEntity(Iterable<AuthorRequestDTO> authorDTOS);
}
