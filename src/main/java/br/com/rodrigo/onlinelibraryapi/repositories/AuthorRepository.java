

package br.com.rodrigo.onlinelibraryapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rodrigo.onlinelibraryapi.entities.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {

    
}