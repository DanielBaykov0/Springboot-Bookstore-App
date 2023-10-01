package baykov.daniel.springbootlibraryapp.payload.mapper;

import baykov.daniel.springbootlibraryapp.entity.Audiobook;
import baykov.daniel.springbootlibraryapp.entity.Category;
import baykov.daniel.springbootlibraryapp.payload.dto.request.AudiobookRequestDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.response.AudiobookResponseDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AudiobookMapper {

    AudiobookMapper INSTANCE = Mappers.getMapper(AudiobookMapper.class);

    @Mapping(target = "authorNames", expression = "java(mapAuthorsNames(audiobook))")
    @Mapping(target = "categoryNames", expression = "java(mapCategoriesNames(audiobook))")
    AudiobookResponseDTO entityToDTO(Audiobook audiobook);

    List<AudiobookResponseDTO> entityToDTO(Iterable<Audiobook> audiobooks);

    Audiobook dtoToEntity(AudiobookRequestDTO audiobookRequestDTO);

    List<Audiobook> dtoToEntity(Iterable<AudiobookRequestDTO> audiobookDTOS);

    default List<String> mapAuthorsNames(Audiobook audiobook) {
        return audiobook.getAuthors().stream()
                .map(author -> author.getFirstName() + " " + author.getLastName())
                .collect(Collectors.toList());
    }

    default List<String> mapCategoriesNames(Audiobook audiobook) {
        return audiobook.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }
}
