package br.com.rodrigo.onlinelibraryapi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rodrigo.onlinelibraryapi.dtos.profile.ListProfileDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.user.CreateUserDto;
import br.com.rodrigo.onlinelibraryapi.dtos.user.ListUserDto;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.mapper.UserMapper;
import br.com.rodrigo.onlinelibraryapi.services.UserService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/profile")
@Slf4j
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public ResponseEntity<ListProfileDTO> index(@AuthenticationPrincipal User data) {
        User user = this.userService.findById(data.getId());
        return ResponseEntity.ok(new ListProfileDTO(user));
    }

    @PutMapping
    public ResponseEntity<ListProfileDTO> update(@RequestBody CreateUserDto data, @AuthenticationPrincipal User auth) {
        return ResponseEntity.ok().body(new ListProfileDTO(userService.update(auth.getId(), data)));

    }

}
