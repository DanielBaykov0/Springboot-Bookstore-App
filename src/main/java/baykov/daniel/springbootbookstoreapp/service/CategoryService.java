package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Category;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.CategoryDTO;
import baykov.daniel.springbootbookstoreapp.payload.mapper.CategoryMapper;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.CategoryRepository;
import baykov.daniel.springbootbookstoreapp.service.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.CATEGORY;
import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ServiceUtil serviceUtil;

    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        log.info("Creating category...");
        Category category = CategoryMapper.INSTANCE.dtoToEntity(categoryDTO);
        categoryRepository.save(category);
        log.info("Created category with ID: {}", category.getId());
        return CategoryMapper.INSTANCE.entityToDTO(category);
    }

    public CategoryDTO getCategoryById(Long categoryId) {
        log.info("Getting category by ID: {}", categoryId);
        Category foundCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, categoryId));
        log.info("Category with ID {} retrieved successfully.", categoryId);
        return CategoryMapper.INSTANCE.entityToDTO(foundCategory);
    }

    public GenericResponse<CategoryDTO> getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving categories...");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Category> categories = categoryRepository.findAll(pageable);
        List<CategoryDTO> content = CategoryMapper.INSTANCE.entityToDTO(categories.getContent());
        log.info("Retrieved {} categories.", categories.getContent().size());
        return serviceUtil.createGenericResponse(categories, content);
    }

    @Transactional
    public CategoryDTO updateCategoryById(Long categoryId, CategoryDTO categoryDTO) {
        log.info("Start updating category with ID: {}", categoryId);
        Category foundCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, categoryId));
        foundCategory.setName(categoryDTO.getName());
        categoryRepository.save(foundCategory);
        log.info("Category with ID {} updated successfully.", categoryId);
        return CategoryMapper.INSTANCE.entityToDTO(foundCategory);
    }

    @Transactional
    public void deleteCategoryById(Long categoryId) {
        log.info("Deleting category with ID: {}", categoryId);
        Category foundCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, categoryId));
        categoryRepository.delete(foundCategory);
        log.info("Category with ID {} deleted successfully.", categoryId);
    }
}
