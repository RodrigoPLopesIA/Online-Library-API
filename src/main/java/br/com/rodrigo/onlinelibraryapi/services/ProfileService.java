package br.com.rodrigo.onlinelibraryapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.rodrigo.onlinelibraryapi.dtos.profile.CreateProfileDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.profile.UpdatePasswordDTO;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProfileService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User findById(String id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(String.format("User %s not found!", id));
        });
        return user;
    }

    public User update(String id, CreateProfileDTO userDetails) {
        User user = this.userRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(String.format("User %s not found!", id));
        });

        user.update(userDetails);
        user.getAuthentication().setPassword(passwordEncoder.encode(userDetails.password()));

        return this.userRepository.save(user);
    }

    public User updatePassword(String id, UpdatePasswordDTO data) {
        User user = this.userRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(String.format("User %s not found!", id));
        });

        if (!passwordEncoder.matches(data.currentPassword(), user.getPassword()))
            throw new IllegalArgumentException("Wrong current password!");

        if (!data.newPassword().equals(data.confirmPassword()))
            throw new IllegalArgumentException("Diferents password!");


        user.getAuthentication().setPassword(passwordEncoder.encode(data.newPassword()));


        this.userRepository.save(user);
        return user;
    }

}
