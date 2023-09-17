package baykov.daniel.springbootlibraryapp.payload.mapper;

import baykov.daniel.springbootlibraryapp.entity.Author;
import baykov.daniel.springbootlibraryapp.payload.dto.AuthorDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    @Mapping(target = "countryId", expression = "java(author.getCountry().getId())")
    @Mapping(target = "cityId", expression = "java(author.getCity().getId())")
    AuthorDTO entityToDto(Author author);

    List<AuthorDTO> entityToDto(Iterable<Author> authors);

    Author dtoToEntity(AuthorDTO authorDTO);

    List<Author> dtoToEntity(Iterable<AuthorDTO> authorDTOS);
}
