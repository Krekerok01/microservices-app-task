package com.specificgroup.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postsSeqGenerator")
    @SequenceGenerator(name = "postsSeqGenerator", sequenceName = "users_seq", allocationSize = 1)
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
