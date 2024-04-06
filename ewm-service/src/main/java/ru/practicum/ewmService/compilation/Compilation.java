package ru.practicum.ewmService.compilation;

import ru.practicum.ewmService.event.Event;

import javax.persistence.*;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pinned")
    private boolean pinned;
    @Column(name = "title")
    private String title;

    @ManyToMany
    @JoinTable(
            name = "event_compilation",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<Event> events;
}
