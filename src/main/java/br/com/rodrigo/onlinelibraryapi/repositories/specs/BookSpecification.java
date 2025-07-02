package br.com.rodrigo.onlinelibraryapi.repositories.specs;

import org.springframework.data.jpa.domain.Specification;

import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.enums.Genre;

public class BookSpecification {

    public static Specification<Book> conjution() {
        return (root, query, cb) -> cb.conjunction();
    }
    public static Specification<Book> titleContains(String title) {
        return (root, query, cb) -> title == null ? null
                : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Book> isbnContains(String isbn) {
        return (root, query, cb) -> isbn == null ? null
                : cb.like(cb.lower(root.get("isbn")), "%" + isbn.toLowerCase() + "%");
    }
    public static Specification<Book> genreEquals(Genre genre) {
    return (root, query, cb) -> genre == null ? null
            : cb.equal(root.get("genre"), genre);
}
    public static Specification<Book> authorNameContains(String authorName) {
        return (root, query, cb) -> {
            if (authorName == null)
                return null;
            return cb.like(cb.lower(root.join("author").get("name")), "%" + authorName.toLowerCase() + "%");
        };
    }

}
