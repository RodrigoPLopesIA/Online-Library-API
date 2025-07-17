package br.com.rodrigo.onlinelibraryapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.rodrigo.onlinelibraryapi.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, String>{
    
}
