package br.com.rodrigo.onlinelibraryapi.entities.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Name {
    

    @Column(nullable = false)
    private String first_name;

    @Column(nullable = false)
    private String last_name;
}
