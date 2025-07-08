package br.com.rodrigo.onlinelibraryapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.rodrigo.onlinelibraryapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rodrigo.onlinelibraryapi.dtos.user.CreateUserDto;
import br.com.rodrigo.onlinelibraryapi.dtos.user.ListUserDto;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.exceptions.UniqueViolationException;
import br.com.rodrigo.onlinelibraryapi.mapper.UserMapper;

@Tag(name = "Users", description = "managing user-related operations in the Online Library API. Provides endpoints to create, retrieve, update, and delete users.")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @SecurityRequirement(name = "")
    @Operation(summary = "Creates a new user", responses = {
            @ApiResponse(responseCode = "201", description = "Create a new user successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListUserDto.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UniqueViolationException.class))),
            @ApiResponse(responseCode = "422", description = "Recurso não processado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MethodArgumentNotValidException.class))),
    })
    @PostMapping
    public ResponseEntity<ListUserDto> create(@Valid @RequestBody CreateUserDto user, UriComponentsBuilder builder) {
        User createdUser = userService.save(user);

        var uri = builder.path("/api/v1/user/{id}").buildAndExpand(createdUser.getId()).toUri();
        return ResponseEntity.created(uri).body(userMapper.toListUserDTO(createdUser));
    }

    @Operation(summary = "Get all users", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ListUserDto.class))))
    })
    @GetMapping
    public ResponseEntity<Page<ListUserDto>> index(Pageable page,
            @PathVariable(value = "firstName", required = false) String firstName,
            @PathVariable(value = "lastName", required = false) String lastName,
            @PathVariable(value = "email", required = false) String email) {

        return ResponseEntity.ok(userService.findAll(page, firstName, lastName, email).map(userMapper::toListUserDTO));
    }

    @Operation(summary = "Retrieves a user by id", responses = {
            @ApiResponse(responseCode = "200", description = "Retrieves a user successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListUserDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundException.class))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ListUserDto> show(@PathVariable String id) {
        User user = userService.findById(id);
        return ResponseEntity.ok().body(userMapper.toListUserDTO(user));
    }

}
