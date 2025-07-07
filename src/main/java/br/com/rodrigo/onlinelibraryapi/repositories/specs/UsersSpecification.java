package br.com.rodrigo.onlinelibraryapi.repositories.specs;

import org.springframework.data.jpa.domain.Specification;

import br.com.rodrigo.onlinelibraryapi.entities.User;


public class UsersSpecification {

    public static Specification<User> conjunction() {
        return (root, query, cb) -> cb.conjunction();
    }

    public static Specification<User> firstNameContains(String first_name) {
        return (root, query, cb) -> first_name == null ? null
                : cb.like(cb.lower(root.get("name").get("first_name")),
                        "%" + first_name.toLowerCase() + "%");
    }

    public static Specification<User> lastNameContains(String last_name) {
        return (root, query, cb) -> last_name == null ? null
                : cb.like(cb.lower(root.get("name").get("last_name")),
                        "%" + last_name.toLowerCase() + "%");
    }

    public static Specification<User> emailContains(String email) {
        return (root, query, cb) -> email == null ? null
                : cb.like(cb.lower(root.get("authentication").get("email")),
                        "%" + email.toLowerCase() + "%");
    }

}
