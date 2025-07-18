package br.com.rodrigo.onlinelibraryapi.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import br.com.rodrigo.onlinelibraryapi.enums.Genre;

@Entity
@Table(name = "books")
@Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 20)
    private String isbn;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Genre genre;

    @Column(precision = 15, scale = 2)
    private BigDecimal price;

    @Column(length = 512)
    private String bookFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_author", nullable = false)
    private Author author;

    @OneToMany(mappedBy = "book")
    List<Category> categories = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;

    // Builder implementation
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String isbn;
        private String title;
        private LocalDate publicationDate;
        private Genre genre;
        private BigDecimal price;
        private Author author;
        private User user;
        private Instant createdAt;
        private Instant updatedAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder publicationDate(LocalDate publicationDate) {
            this.publicationDate = publicationDate;
            return this;
        }

        public Builder genre(Genre genre) {
            this.genre = genre;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder author(Author author) {
            this.author = author;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Book build() {
            Book book = new Book();
            book.id = this.id;
            book.isbn = this.isbn;
            book.title = this.title;
            book.publicationDate = this.publicationDate;
            book.genre = this.genre;
            book.price = this.price;
            book.author = this.author;
            book.user = this.user;
            book.createdAt = this.createdAt;
            book.updatedAt = this.updatedAt;
            return book;
        }
    }
}