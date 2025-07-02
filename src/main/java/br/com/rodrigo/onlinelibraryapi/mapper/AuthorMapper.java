package br.com.rodrigo.onlinelibraryapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.rodrigo.onlinelibraryapi.dtos.author.CreateAuthorDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.author.ListAuthorDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {



    Author toAuthor(ListAuthorDTO authorDTO);
    Author toAuthor(CreateAuthorDTO authorDTO);

    ListAuthorDTO toListAuthorDTO(Author author);
    CreateAuthorDTO toCreateAuthorDTO(Author author);

    
}
