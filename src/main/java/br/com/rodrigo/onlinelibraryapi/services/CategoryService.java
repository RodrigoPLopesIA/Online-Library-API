package br.com.rodrigo.onlinelibraryapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import br.com.rodrigo.onlinelibraryapi.dtos.category.CreateCategoryDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.category.ListCategoryDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Category;
import br.com.rodrigo.onlinelibraryapi.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<ListCategoryDTO> index(Pageable page, String name) {

        Category category = Category.builder().name(name).build();

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(StringMatcher.CONTAINING);

        Example<Category> example = Example.of(category, matcher);

        return this.categoryRepository.findAll(example, page).map(ListCategoryDTO::new);

    }

    public ListCategoryDTO save(CreateCategoryDTO dto) {
        var entity = Category.builder().name(dto.name()).build();

        if (categoryRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("this category already exists");
        }
        return new ListCategoryDTO(this.categoryRepository.save(entity));

    }

    public ListCategoryDTO update(String id, CreateCategoryDTO data) {
        var category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category %s not found!", id)));

        boolean nameAlreadyExists = categoryRepository.existsByName(data.name())
                && !category.getName().equalsIgnoreCase(data.name());

        if (nameAlreadyExists) {
            throw new IllegalArgumentException(String.format("Category %s already exists!", data.name()));
        }

        category.setName(data.name());
        this.categoryRepository.save(category);
        return new ListCategoryDTO(category);
    }
}
