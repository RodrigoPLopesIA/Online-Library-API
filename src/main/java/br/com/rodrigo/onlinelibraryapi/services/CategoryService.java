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

    public Object save(CreateCategoryDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }
}
