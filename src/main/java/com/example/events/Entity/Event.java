package com.example.events.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="EVENT")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(unique = true)
    private String name;
    private LocalDate date;
    private String location;
    private String description;

    @ManyToOne
    @JoinColumn(name = "organizer_id", referencedColumnName = "id")
    private User organizer;
}