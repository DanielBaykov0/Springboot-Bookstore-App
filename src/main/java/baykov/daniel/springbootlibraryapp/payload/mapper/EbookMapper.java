package baykov.daniel.springbootlibraryapp.payload.mapper;

import baykov.daniel.springbootlibraryapp.entity.Category;
import baykov.daniel.springbootlibraryapp.entity.Ebook;
import baykov.daniel.springbootlibraryapp.payload.dto.request.EbookRequestDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.response.EbookResponseDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EbookMapper {

    EbookMapper INSTANCE = Mappers.getMapper(EbookMapper.class);

    @Mapping(target = "authorNames", expression = "java(mapAuthorsNames(ebook))")
    @Mapping(target = "categoryNames", expression = "java(mapCategoriesNames(ebook))")
    EbookResponseDTO entityToDTO(Ebook ebook);

    List<EbookResponseDTO> entityToDTO(Iterable<Ebook> ebooks);

    Ebook dtoToEntity(EbookRequestDTO ebookRequestDTO);

    List<Ebook> dtoToEntity(Iterable<EbookRequestDTO> ebookDTOS);

    default List<String> mapAuthorsNames(Ebook ebook) {
        return ebook.getAuthors().stream()
                .map(author -> author.getFirstName() + " " + author.getLastName())
                .collect(Collectors.toList());
    }

    default List<String> mapCategoriesNames(Ebook ebook) {
        return ebook.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }
}
