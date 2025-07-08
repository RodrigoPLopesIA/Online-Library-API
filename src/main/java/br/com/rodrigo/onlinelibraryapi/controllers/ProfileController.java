package br.com.rodrigo.onlinelibraryapi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rodrigo.onlinelibraryapi.dtos.profile.CreateProfileDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.profile.ListProfileDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.profile.UpdatePasswordDTO;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.services.ProfileService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/profile")
@Slf4j
public class ProfileController {

    @Autowired
    private ProfileService profileService;


    @GetMapping
    public ResponseEntity<ListProfileDTO> index(@AuthenticationPrincipal User data) {
        User user = this.profileService.findById(data.getId());
        return ResponseEntity.ok(new ListProfileDTO(user));
    }

    @PutMapping
    public ResponseEntity<ListProfileDTO> update(@Valid @RequestBody CreateProfileDTO data, @AuthenticationPrincipal User auth) {
        return ResponseEntity.ok().body(new ListProfileDTO(profileService.update(auth.getId(), data)));

    }

    @PatchMapping
    public ResponseEntity<ListProfileDTO> updatePassword(@Valid @RequestBody UpdatePasswordDTO data, @AuthenticationPrincipal User auth) {
        return ResponseEntity.ok().body(new ListProfileDTO(profileService.updatePassword(auth.getId(), data)));

    }

}
