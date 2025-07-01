

package br.com.rodrigo.onlinelibraryapi.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rodrigo.onlinelibraryapi.entities.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {


    Optional<Author> findByName(String name);
    
    // Additional query methods can be defined here if needed
    
}