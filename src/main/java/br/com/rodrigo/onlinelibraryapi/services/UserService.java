package br.com.rodrigo.onlinelibraryapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.rodrigo.onlinelibraryapi.dtos.user.CreateUserDto;
import br.com.rodrigo.onlinelibraryapi.dtos.user.ListUserDto;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.exceptions.UniqueViolationException;
import br.com.rodrigo.onlinelibraryapi.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Page<ListUserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(ListUserDto::new);
    }

    public ListUserDto findById(String id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(String.format("User %s not found!", id));
        });
        return new ListUserDto(user);
    }

    public ListUserDto save(CreateUserDto user) {
        try {
            User newUser = userRepository.save(new User(user));
            return new ListUserDto(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException(String.format("user %s already registered", user.email()));
        }
    }

    public ListUserDto update(String id, CreateUserDto userDetails) {
        User user = this.userRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(String.format("User %s not found!", id));
        });
        user.update(userDetails);

        return new ListUserDto(this.userRepository.save(user));
    }

    public void delete(String id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(String.format("User %s not found!", id));
        });
        userRepository.delete(user);
    }

}
