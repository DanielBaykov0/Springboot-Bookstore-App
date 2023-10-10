package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Category;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.CategoryDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.CategoryRepository;
import baykov.daniel.springbootbookstoreapp.service.util.ServiceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ServiceUtil serviceUtil;

    @Captor
    private ArgumentCaptor<Category> categoryArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository, serviceUtil);
    }

    @Test
    void testCreateCategory_ReturnCategoryDTO() {
        CategoryDTO categoryDTO = new CategoryDTO();

        Category returnObject = new Category();
        when(categoryRepository.save(categoryArgumentCaptor.capture())).thenReturn(returnObject);

        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        Assertions.assertNotNull(createdCategory);

        Category category = categoryArgumentCaptor.getValue();
        Assertions.assertNotNull(category);
        Assertions.assertEquals(category.getName(), categoryDTO.getName());
    }

    @Test
    void testGetCategoryById_ReturnCategoryDTO() {
        Long categoryId = 1L;

        Category returnObject = new Category();
        when(categoryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));

        CategoryDTO retrievedCategory = categoryService.getCategoryById(categoryId);
        Assertions.assertNotNull(retrievedCategory);

        Long capturedCategoryId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedCategoryId);
        Assertions.assertEquals(capturedCategoryId, categoryId);
    }

    @Test
    void testGetCategoryById_ReturnCategoryException() {
        Long categoryId = 1L;

        when(categoryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getCategoryById(categoryId)
        );

        Assertions.assertEquals("Category not found with ID : '1'", exception.getMessage());

        Long capturedCategoryId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedCategoryId);
        Assertions.assertEquals(capturedCategoryId, categoryId);
    }

    @Test
    void testGetAllCategories_SortASC_ReturnCategoryDTO() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = "ASC";

        Page<Category> mockPage = new PageImpl<>(List.of());
        List<CategoryDTO> content = new ArrayList<>();
        GenericResponse<CategoryDTO> mockResponse = new GenericResponse<>();
        when(categoryRepository.findAll(pageableArgumentCaptor.capture())).thenReturn(mockPage);
        when(serviceUtil.createGenericResponse(mockPage, content)).thenReturn(mockResponse);

        GenericResponse<CategoryDTO> retrievedCategoryResponse = categoryService.getAllCategories(pageNo, pageSize, sortBy, sortDir);
        Assertions.assertNotNull(retrievedCategoryResponse);

        Pageable pageable = pageableArgumentCaptor.getValue();
        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageNo, pageable.getPageNumber());
        Assertions.assertEquals(pageSize, pageable.getPageSize());
        Assertions.assertEquals(sortBy, pageable.getSort().get().findFirst().map(Sort.Order::getProperty).orElse(null));
        Assertions.assertEquals(sortDir, pageable.getSort().get().findFirst().map(order -> order.getDirection().toString()).orElse(null));
    }

    @Test
    void testUpdateCategoryById_ReturnCategoryDTO() {
        Long categoryId = 1L;
        CategoryDTO updatedDto = new CategoryDTO();

        Category returnObject = new Category();
        when(categoryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));
        when(categoryRepository.save(categoryArgumentCaptor.capture())).thenReturn(returnObject);

        CategoryDTO updatedCategory = categoryService.updateCategoryById(categoryId, updatedDto);
        Assertions.assertNotNull(updatedCategory);

        Category category = categoryArgumentCaptor.getValue();
        Assertions.assertNotNull(category);
        Assertions.assertEquals(category.getName(), updatedDto.getName());

        Long categoryIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(categoryIdCaptor);
        Assertions.assertEquals(categoryIdCaptor, categoryId);

        verify(categoryRepository).save(categoryArgumentCaptor.capture());
    }

    @Test
    void testUpdateCategoryById_ReturnCategoryException() {
        Long categoryId = 1L;
        CategoryDTO updatedDto = new CategoryDTO();

        when(categoryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.updateCategoryById(categoryId, updatedDto)
        );

        Assertions.assertEquals("Category not found with ID : '1'", exception.getMessage());

        Long categoryIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(categoryIdCaptor);
        Assertions.assertEquals(categoryIdCaptor, categoryId);

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void testDeleteCategoryById_Success() {
        Long categoryId = 1L;

        Category categoryToDelete = new Category();
        when(categoryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(categoryToDelete));

        categoryService.deleteCategoryById(categoryId);

        Long capturedCategoryId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedCategoryId);
        Assertions.assertEquals(capturedCategoryId, categoryId);

        verify(categoryRepository).delete(categoryToDelete);
    }

    @Test
    void testDeleteCategoryById_ReturnCategoryException() {
        Long categoryId = 1L;

        when(categoryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.deleteCategoryById(categoryId)
        );

        Assertions.assertEquals("Category not found with ID : '1'", exception.getMessage());

        Long capturedCategoryId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedCategoryId);
        Assertions.assertEquals(capturedCategoryId, categoryId);

        verify(categoryRepository, never()).delete(any());
    }
}