package br.com.rodrigo.onlinelibraryapi.mapper;

import org.mapstruct.Mapper;

import br.com.rodrigo.onlinelibraryapi.dtos.books.CreateBookDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.books.ListBookDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {



    Book toEntity(ListBookDTO bookDto);
    Book toEntity(CreateBookDTO bookDto);
    
    ListBookDTO toDto(Book book);
    
}
