package br.com.rodrigo.onlinelibraryapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.rodrigo.onlinelibraryapi.dtos.author.CreateAuthorDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.author.ListAuthorDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {


    @Mapping(target = "books", ignore = true)
    Author toAuthor(ListAuthorDTO authorDTO);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Author toAuthor(CreateAuthorDTO authorDTO);

   
    ListAuthorDTO toListAuthorDTO(Author author);
    CreateAuthorDTO toCreateAuthorDTO(Author author);

    
}
