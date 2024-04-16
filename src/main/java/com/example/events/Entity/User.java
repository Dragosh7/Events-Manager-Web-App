package com.example.events.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "first_name", nullable = false)
    @Size(max = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Size(max = 100)
    private String lastName;

    @Column(nullable = false,unique = true)
    @Size(max = 100)
    private String username;

    @Column(nullable = false)
    @Size(max = 100)
    @JsonIgnore
    private String password;
    @NotBlank
    @NaturalId(mutable = true)
    private String emailAddress;
    private String role ;
    @JsonIgnore
    private boolean active;
    @Builder.Default
    @JsonIgnore
    private Boolean accountNonExpired = true;

    @Builder.Default
    @JsonIgnore
    private Boolean accountNonLocked = true;

    @Builder.Default
    @JsonIgnore
    private Boolean credentialsNonExpired = true;

    @Builder.Default
    @JsonIgnore
    private Boolean enabled = true;

    public void subscribeToEvent(Event event) {
        subscribedEvents.add(event);
    }

    @Transient
    private List<Event> subscribedEvents = new ArrayList<>();
    public void unsubscribeFromEvent(Event event) {
        subscribedEvents.remove(event);
    }

    public List<Event> getSubscribedEvents() {
        return subscribedEvents;
    }

}