package br.com.rodrigo.onlinelibraryapi.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.rodrigo.onlinelibraryapi.dtos.profile.CreateProfileDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.user.CreateUserDto;
import br.com.rodrigo.onlinelibraryapi.entities.embedded.Address;
import br.com.rodrigo.onlinelibraryapi.entities.embedded.Authentication;
import br.com.rodrigo.onlinelibraryapi.entities.embedded.Name;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Embedded
    private Name name;

    @Embedded
    private Authentication authentication;

    @Embedded
    private Address address;


    @OneToMany(mappedBy = "user")
    List<Book> books = new ArrayList<>();
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    public User(CreateUserDto user) {
        this.update(user);
    }

    public void update(CreateUserDto user) {
        this.setName(new Name(user.first_name(), user.last_name()));
        this.setAuthentication(new Authentication(user.email(), user.provider()));
        this.setAddress(new Address(user.street(), user.number(), user.complement(), user.neighborhood(), user.city(),
                user.state(), user.zipCode()));
    }
    public void update(CreateProfileDTO user) {
        this.setName(new Name(user.first_name(), user.last_name()));
        this.setAuthentication(new Authentication(user.email(), ""));
        this.setAddress(new Address(user.street(), user.number(), user.complement(), user.neighborhood(), user.city(),
                user.state(), user.zipCode()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assuming no specific roles or authorities are defined for this user
        return null; // or Collections.emptyList() if you prefer
    }

    @Override
    public String getPassword() {
        return this.authentication.getPassword();
    }

    @Override
    public String getUsername() {
        return this.authentication.getEmail();
    }

}
