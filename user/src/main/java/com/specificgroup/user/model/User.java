package com.specificgroup.user.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private String password;

    @Email(message = "Email pattern should be: *@*.*")
    private String email;
    private Role role;


    @RequiredArgsConstructor
    @Getter
    public enum Role {
        DEFAULT("Default"),
        ADMIN("Admin");

        private final String name;
    }
}
