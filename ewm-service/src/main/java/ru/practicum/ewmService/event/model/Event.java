package ru.practicum.ewmService.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import ru.practicum.ewmService.category.model.Category;
import ru.practicum.ewmService.event.enums.EventState;
import ru.practicum.ewmService.location.model.Location;
import ru.practicum.ewmService.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation")
    private String annotation;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(name = "confirmed_requests")
    private long confirmedRequests = 0;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Nullable
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User initiator;

    @Column(name = "paid")
    private boolean paid;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state = EventState.PENDING;

    @Column(name = "participant_limit")
    private int participantLimit;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    Location location;

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", annotation='" + annotation + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", confirmedRequests=" + confirmedRequests +
                ", eventDate=" + eventDate +
                ", createdOn=" + createdOn +
                ", publishedOn=" + publishedOn +
                ", initiator=" + initiator +
                ", paid=" + paid +
                ", requestModeration=" + requestModeration +
                ", state=" + state +
                ", participantLimit=" + participantLimit +
                ", location=" + location +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        return this.id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return 15;
    }
}


