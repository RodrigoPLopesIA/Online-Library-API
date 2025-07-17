package br.com.rodrigo.onlinelibraryapi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rodrigo.onlinelibraryapi.dtos.category.ListCategoryDTO;
import br.com.rodrigo.onlinelibraryapi.services.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<ListCategoryDTO>> index(Pageable page) {
        Page<ListCategoryDTO> index = categoryService.index(page);
        return ResponseEntity.ok().body(index);
    }
    
}
