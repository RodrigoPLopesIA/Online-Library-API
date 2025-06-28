package br.com.rodrigo.onlinelibraryapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rodrigo.onlinelibraryapi.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    
}
