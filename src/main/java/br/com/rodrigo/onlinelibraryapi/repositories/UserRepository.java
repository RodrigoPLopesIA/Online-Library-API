package br.com.rodrigo.onlinelibraryapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.rodrigo.onlinelibraryapi.entities.User;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User>{
    User findByAuthenticationEmail(String email);
}
