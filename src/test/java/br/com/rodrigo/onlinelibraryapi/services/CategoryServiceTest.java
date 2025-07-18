package br.com.rodrigo.onlinelibraryapi.services;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;

import br.com.rodrigo.onlinelibraryapi.dtos.category.CreateCategoryDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.category.ListCategoryDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Category;
import br.com.rodrigo.onlinelibraryapi.repositories.CategoryRepository;
import org.springframework.data.domain.Example;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName("should return all categories")
    public void shouldReturnAllCategories() {
        // Arrange
        Category category = Category.builder()
                .name("test")
                .id(UUID.randomUUID().toString())
                .build();

        ListCategoryDTO categoryDTO = new ListCategoryDTO(category);

        Page<Category> categoryPage = new PageImpl<>(List.of(category), PageRequest.of(0, 100), 1);
        Page<ListCategoryDTO> categoryPageDTO = new PageImpl<>(List.of(categoryDTO), PageRequest.of(0, 100), 1);

        Pageable pageable = PageRequest.of(0, 100, Sort.unsorted());
        String search = "test";

        Mockito.when(categoryRepository.findAll(Mockito.any(Example.class), Mockito.eq(pageable)))
                .thenReturn(categoryPage);

        // Act
        var result = categoryService.index(pageable, search);

        // Assert
        assertThat(result).isEqualTo(categoryPageDTO);
    }

    @Test
    @DisplayName("Should create new category")
    public void shouldCreateNewCategory(){
        CreateCategoryDTO dto = new CreateCategoryDTO("TEST");

        var result = categoryService.save(dto);


        assertThat(result).isNotNull();
    }
}
