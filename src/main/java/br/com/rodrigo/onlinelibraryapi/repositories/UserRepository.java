package br.com.rodrigo.onlinelibraryapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rodrigo.onlinelibraryapi.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

}
