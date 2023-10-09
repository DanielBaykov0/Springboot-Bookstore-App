package baykov.daniel.springbootbookstoreapp.payload.mapper;

import baykov.daniel.springbootbookstoreapp.entity.Audiobook;
import baykov.daniel.springbootbookstoreapp.entity.Category;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.AudiobookRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.AudiobookResponseDTO;
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
    @Mapping(target = "narratorId", expression = "java(audiobook.getNarrator().getId())")
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
