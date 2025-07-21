package br.com.rodrigo.onlinelibraryapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.rodrigo.onlinelibraryapi.dtos.books.CreateBookDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.books.ListBookDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.repositories.AuthorRepository;
import br.com.rodrigo.onlinelibraryapi.repositories.CategoryRepository;

@Mapper(componentModel = "spring")
public abstract class BookMapper {

    @Autowired
    protected AuthorRepository authorRepository;

    @Autowired
    protected CategoryRepository categoryRepository;
    

    @Mapping(target = "author", expression = "java(authorRepository.findById(bookDto.author().id()).orElse(null))")
    public abstract Book toEntity(ListBookDTO bookDto);

    @Mapping(target = "author", expression = "java(authorRepository.findById(bookDto.authorId()).orElse(null))")
    public abstract Book toEntity(CreateBookDTO bookDto);

    public abstract ListBookDTO toDto(Book book);

}
