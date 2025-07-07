package br.com.rodrigo.onlinelibraryapi.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import br.com.rodrigo.onlinelibraryapi.dtos.books.CreateBookDTO;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_author", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable =  false)
    private User user;

    
    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;



    public void update(Book data) {
        this.isbn = data.getIsbn();
        this.title = data.getTitle();
        this.publicationDate = data.getPublicationDate();
        this.genre = data.getGenre();
        this.price = data.getPrice();
    }

    public Book(CreateBookDTO data) {
        this.isbn = data.isbn();
        this.title = data.title();
        this.publicationDate = data.publicationDate();
        this.genre = data.genre();
        this.price = data.price();

    }
}

