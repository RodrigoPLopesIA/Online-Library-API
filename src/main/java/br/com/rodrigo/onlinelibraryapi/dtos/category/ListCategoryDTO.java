package br.com.rodrigo.onlinelibraryapi.dtos.category;

import br.com.rodrigo.onlinelibraryapi.entities.Category;

public record ListCategoryDTO(String id, String name) {

    public ListCategoryDTO(Category category) {
        this(category.getId(), category.getName());
    }
}
