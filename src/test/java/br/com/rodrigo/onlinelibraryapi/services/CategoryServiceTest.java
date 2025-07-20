package br.com.rodrigo.onlinelibraryapi.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import br.com.rodrigo.onlinelibraryapi.dtos.category.CreateCategoryDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.category.ListCategoryDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Category;
import br.com.rodrigo.onlinelibraryapi.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Example;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    private CreateCategoryDTO createDto;

    private ListCategoryDTO listDto;

    private Page<Category> categoryPage;

    private Page<ListCategoryDTO> categoryPageDTO;

    private Pageable pageable;

    private String categoryId;

    @BeforeEach
    public void setup() {
        category = Category.builder()
                .name("test")
                .id("d6e4331d-dd66-49b4-9be0-e0372a8f5013")
                .build();
        createDto = new CreateCategoryDTO(category.getName());

        listDto = new ListCategoryDTO(category);

        categoryPage = new PageImpl<>(List.of(category), PageRequest.of(0, 100), 1);
        categoryPageDTO = new PageImpl<>(List.of(listDto), PageRequest.of(0, 100), 1);
        pageable = PageRequest.of(0, 100, Sort.unsorted());
        categoryId = "d6e4331d-dd66-49b4-9be0-e0372a8f5013";
    }

    @Test
    @DisplayName("should return all categories")
    public void shouldReturnAllCategories() {
        // Arrange
        String search = createDto.name();

        Mockito.when(categoryRepository.findAll(Mockito.any(Example.class), Mockito.eq(pageable)))
                .thenReturn(categoryPage);

        // Act
        var result = categoryService.index(pageable, search);

        // Assert
        assertThat(result).isEqualTo(categoryPageDTO);
    }

    @Test
    @DisplayName("Should create new category")
    public void shouldCreateNewCategory() {

        Mockito.when(categoryRepository.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);

        var result = categoryService.save(createDto);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(category.getId());
        assertThat(result.name()).isEqualTo(category.getName());

        Mockito.verify(categoryRepository, times(1)).existsByName(Mockito.anyString());
        Mockito.verify(categoryRepository, times(1)).save(Mockito.any(Category.class));
    }

    @Test
    @DisplayName("Should return a error when try to create a new category")
    public void shouldReturnErrorWhenCreateACategoryAlreadyExists() {

        Mockito.when(categoryRepository.existsByName(Mockito.anyString())).thenReturn(true);

        var result = assertThrows(IllegalArgumentException.class, () -> categoryService.save(createDto));

        assertThat(result).isInstanceOf(IllegalArgumentException.class);
        assertThat(result.getMessage()).isEqualTo("this category already exists");

        Mockito.verify(categoryRepository, times(1)).existsByName(Mockito.anyString());
        Mockito.verify(categoryRepository, never()).save(Mockito.any(Category.class));
    }

    @Test
    @DisplayName("should update a category")
    public void shouldUpdateCategory() {
        // Arrange

        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        Mockito.when(categoryRepository.save(Mockito.any(Category.class)))
                .thenReturn(category);

        // Act
        var result = categoryService.update(categoryId, createDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(categoryId);
        assertThat(result.name()).isEqualTo(createDto.name());

        Mockito.verify(categoryRepository, times(1)).findById(categoryId);
        Mockito.verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("should return a error when try to update a category not exists")
    public void shouldUpdateCategoryNotExists() {
        // Arrange

        Mockito.when(categoryRepository.findById(categoryId))
                .thenThrow(new EntityNotFoundException(String.format("Category %s not found!", categoryId)));

        // Act
        var result = catchThrowable(() -> categoryService.update(categoryId, createDto));

        // Assert
        assertThat(result).isInstanceOf(EntityNotFoundException.class);
        assertThat(result.getMessage()).isEqualTo(String.format("Category %s not found!", categoryId));

        Mockito.verify(categoryRepository, times(1)).findById(categoryId);
        Mockito.verify(categoryRepository, never()).save(category);
    }

    @Test
    @DisplayName("should return a error when try to update a exists category")
    public void shouldUpdateCategoryAlreadyExists() {
        // Arrange
        Category category = new Category();
        category.setName("Old Name");

        CreateCategoryDTO createDto = new CreateCategoryDTO("New Name");
        
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName("New Name")).thenReturn(true);

        // Act
        var result = catchThrowable(() -> categoryService.update(categoryId, createDto));

        // Assert
        assertThat(result).isInstanceOf(IllegalArgumentException.class);
        assertThat(result.getMessage()).isEqualTo(String.format("Category %s already exists!", createDto.name()));

        Mockito.verify(categoryRepository, times(1)).findById(categoryId);
        Mockito.verify(categoryRepository, never()).save(category);
    }

}
