package baykov.daniel.springbootbookstoreapp.payload.mapper;

import baykov.daniel.springbootbookstoreapp.entity.Category;
import baykov.daniel.springbootbookstoreapp.payload.dto.CategoryDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO entityToDTO(Category category);

    List<CategoryDTO> entityToDTO(Iterable<Category> categories);

    Category dtoToEntity(CategoryDTO categoryDTO);

    List<Category> dtoToEntity(Iterable<CategoryDTO> categoryDTOS);
}
