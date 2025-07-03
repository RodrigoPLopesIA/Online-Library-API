package br.com.rodrigo.onlinelibraryapi.mapper;

import org.mapstruct.Mapper;

import br.com.rodrigo.onlinelibraryapi.dtos.user.CreateUserDto;
import br.com.rodrigo.onlinelibraryapi.dtos.user.ListUserDto;
import br.com.rodrigo.onlinelibraryapi.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {



    User toUser(ListUserDto userDTO);
    User toUser(CreateUserDto userDTO);
    ListUserDto toListUserDTO(User user);
    CreateUserDto toCreateUserDTO(User user);
    
}
