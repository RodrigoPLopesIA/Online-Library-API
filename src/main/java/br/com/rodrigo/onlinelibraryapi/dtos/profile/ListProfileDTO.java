package br.com.rodrigo.onlinelibraryapi.dtos.profile;

import java.time.Instant;
import java.util.List;

import br.com.rodrigo.onlinelibraryapi.dtos.books.ListBookDTO;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.entities.embedded.Address;

public record ListProfileDTO(
        String id,
        String first_name,
        String last_name,
        String email,
        Address address,
        List<ListBookDTO> books,
        Instant created_at,
        Instant updated_at) {

    public ListProfileDTO(User user) {
        this(
            user.getId(), 
            user.getName().getFirst_name(), 
            user.getName().getLast_name(), 
            user.getAuthentication().getEmail(), 
            user.getAddress(), 
            user.getBooks().stream().map(ListBookDTO::new).toList(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}

