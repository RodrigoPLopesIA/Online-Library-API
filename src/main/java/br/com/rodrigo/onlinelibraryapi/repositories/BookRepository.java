package br.com.rodrigo.onlinelibraryapi.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.rodrigo.onlinelibraryapi.entities.Book;


@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    @Query ("SELECT b FROM Book b WHERE b.title LIKE %:title%")
    List<Book> findAllBooksLikeTitle(@Param("title") String title);

    Boolean existsByIsbn(String isbn);

    Boolean existsByTitle(String title);
    
    Page<Book> findByTitleContainingAndIsbnContaining(String title, String isbn, Pageable pageable);


    Page<Book> findByTitleContaining(String title, Pageable pageable);

    Page<Book> findByIsbnContaining(String isbn, Pageable pageable);

    
}
