package br.com.rodrigo.onlinelibraryapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.enums.Genre;
import jakarta.transaction.Transactional;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {


    Boolean existsByIsbn(String isbn);

    Boolean existsByTitle(String title);

    @Query ("SELECT b FROM Book b WHERE b.title LIKE %:title%")
    List<Book> findAllBooksLikeTitle(@Param("title") String title);

    
}
