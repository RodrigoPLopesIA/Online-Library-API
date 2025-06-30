package br.com.rodrigo.onlinelibraryapi.dtos.user;
import br.com.rodrigo.onlinelibraryapi.entities.embedded.Address;
import br.com.rodrigo.onlinelibraryapi.entities.User;

import java.time.Instant;


public record ListUserDto(
    String id,
    String first_name,
    String last_name,
    String email,
    Address address,
    Instant created_at,
    Instant updated_at
) {

    public ListUserDto(User user) {
        this(
            user.getId(),
            user.getName().getFirst_name(),
            user.getName().getLast_name(),
            user.getAuthentication().getEmail(),
            user.getAddress(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
