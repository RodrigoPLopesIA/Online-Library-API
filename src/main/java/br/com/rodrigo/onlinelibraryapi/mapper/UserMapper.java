package br.com.rodrigo.onlinelibraryapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.rodrigo.onlinelibraryapi.dtos.user.CreateUserDto;
import br.com.rodrigo.onlinelibraryapi.dtos.user.ListUserDto;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.entities.embedded.Authentication;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "authentication", expression = "java(new br.com.rodrigo.onlinelibraryapi.entities.embedded.Authentication(userDTO.email()))")

    @Mapping(target = "name", expression = "java(new br.com.rodrigo.onlinelibraryapi.entities.embedded.Name(userDTO.first_name(), userDTO.last_name()))")
    @Mapping(target = "address", expression = "java(new br.com.rodrigo.onlinelibraryapi.entities.embedded.Address(userDTO.address().getStreet(), userDTO.address().getNumber(), userDTO.address().getComplement(), userDTO.address().getNeighborhood(), userDTO.address().getCity(), userDTO.address().getState(), userDTO.address().getZipCode()))")
    User toUser(ListUserDto userDTO);

    @Mapping(target = "authentication", expression = "java(new br.com.rodrigo.onlinelibraryapi.entities.embedded.Authentication(userDTO.email(), userDTO.password(), userDTO.provider()))")
    @Mapping(target = "name", expression = "java(new br.com.rodrigo.onlinelibraryapi.entities.embedded.Name(userDTO.first_name(), userDTO.last_name()))")
    @Mapping(target = "address", expression = "java(new br.com.rodrigo.onlinelibraryapi.entities.embedded.Address(userDTO.street(), userDTO.number(), userDTO.complement(), userDTO.neighborhood(), userDTO.city(), userDTO.state(), userDTO.zipCode()))")
    User toUser(CreateUserDto userDTO);

    @Mapping(target = "first_name", expression = "java(user.getName().getFirst_name())")
    @Mapping(target = "last_name", expression = "java(user.getName().getLast_name())")
    @Mapping(target = "email", expression = "java(user.getAuthentication().getEmail())") 
    @Mapping(target = "address", expression = "java(user.getAddress())")
    @Mapping(target = "created_at", expression = "java(user.getCreatedAt())")
    @Mapping(target = "updated_at", expression = "java(user.getUpdatedAt())")
    ListUserDto toListUserDTO(User user);

    CreateUserDto toCreateUserDTO(User user);

}
