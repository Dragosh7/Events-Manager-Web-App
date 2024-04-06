package com.example.events.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(unique = true, updatable = false)
    private String username;
    @NonNull
    private String password;
    private String role;
    @NonNull
    @NaturalId(mutable = true)
    private String emailAddress;

    private String name;
    private String lastName;
    private String phone;

}
