package br.com.rodrigo.onlinelibraryapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.rodrigo.onlinelibraryapi.dtos.category.ListCategoryDTO;
import br.com.rodrigo.onlinelibraryapi.repositories.CategoryRepository;

@Service
public class CategoryService {



    @Autowired
    private CategoryRepository categoryRepository;


    public Page<ListCategoryDTO> index(Pageable page){
        return this.categoryRepository.findAll(page).map(ListCategoryDTO::new);
        
    }
}
