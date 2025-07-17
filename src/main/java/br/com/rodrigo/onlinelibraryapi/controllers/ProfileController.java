package br.com.rodrigo.onlinelibraryapi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.rodrigo.onlinelibraryapi.dtos.books.ListBookDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.files.UploadFileDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.profile.CreateProfileDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.profile.ListProfileDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.profile.UpdatePasswordDTO;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.exceptions.ErrorMessage;
import br.com.rodrigo.onlinelibraryapi.services.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Profile", description = "managing profile-related operations in the Online Library API. Provides endpoints to retrieve, update, update password and update image")
@RestController
@RequestMapping("/api/v1/profile")
@Slf4j
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Operation(summary = "Get profile", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ListProfileDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Profile not found.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ErrorMessage.class))))
    })
    @GetMapping
    public ResponseEntity<ListProfileDTO> index(@AuthenticationPrincipal User data) {
        User user = this.profileService.findById(data.getId());
        return ResponseEntity.ok(new ListProfileDTO(user));
    }

    @Operation(summary = "Update profile", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully update user password", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ListProfileDTO.class)))),
            @ApiResponse(responseCode = "404", description = "user not found", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ErrorMessage.class)))),
    })
    @PutMapping
    public ResponseEntity<ListProfileDTO> update(@Valid @RequestBody CreateProfileDTO data,
            @AuthenticationPrincipal User auth) {
        return ResponseEntity.ok().body(new ListProfileDTO(profileService.update(auth.getId(), data)));

    }

    @Operation(summary = "Update profile password", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully update user password", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ListProfileDTO.class)))),
            @ApiResponse(responseCode = "404", description = "user not found", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ErrorMessage.class)))),
            @ApiResponse(responseCode = "400", description = "Wrong current password!", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ErrorMessage.class)))),
            @ApiResponse(responseCode = "400", description = "Diferents password", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ErrorMessage.class)))),
    })

    @PatchMapping
    public ResponseEntity<ListProfileDTO> updatePassword(@Valid @RequestBody UpdatePasswordDTO data,
            @AuthenticationPrincipal User auth) {
        return ResponseEntity.ok().body(new ListProfileDTO(profileService.updatePassword(auth.getId(), data)));

    }

    @Operation(summary = "Update profile image", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully update user profile image", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ListProfileDTO.class)))),
    })
    @PostMapping("/upload")
    public ResponseEntity<UploadFileDTO> updateImage(@AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(this.profileService.uploadProfileImage(user, file));

    }

}
