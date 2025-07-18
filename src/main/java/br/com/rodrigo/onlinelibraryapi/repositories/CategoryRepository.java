package br.com.rodrigo.onlinelibraryapi.repositories;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.rodrigo.onlinelibraryapi.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, String>{
}
