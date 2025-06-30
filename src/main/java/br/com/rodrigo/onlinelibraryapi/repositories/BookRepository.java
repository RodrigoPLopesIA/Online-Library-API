package br.com.rodrigo.onlinelibraryapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.entities.Genre;
import jakarta.transaction.Transactional;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {


    Boolean existsByIsbn(String isbn);

    Boolean existsByTitle(String title);

    @Query ("SELECT b FROM Book b WHERE b.title LIKE %:title%")
    List<Book> findAllBooksLikeTitle(@Param("title") String title);

    @Modifying()
    @Transactional()
    @Query("DELETE FROM Book b WHERE b.genre = :genre")
    Book deleteBookByGenre(Genre genre);

    @Modifying()
    @Transactional()
    @Query("UPDATE Book b SET b.title = :book.title, b.publicationDate = :book.publicationDate, b.genre = :book.genre, b.price = :book.price WHERE b.author.id = :author_id")
    Book updateBookByAuthorId(UUID author_id, Book book);
    
}
