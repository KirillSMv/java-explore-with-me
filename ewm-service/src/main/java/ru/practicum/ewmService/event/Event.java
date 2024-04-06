package ru.practicum.ewmService.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;
import ru.practicum.ewmService.category.Category;
import ru.practicum.ewmService.event.enums.EventState;
import ru.practicum.ewmService.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
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

    @ManyToOne(fetch = FetchType.EAGER) //указываю для лучшей читаемости кода
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ToString.Exclude
    private Category category;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests = 0L;

    @Column(name = "event_date")
    private LocalDateTime eventDate; //String

    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now(); //String

    @Nullable
    @Column(name = "published_on")
    private LocalDateTime publishedOn; //String

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User initiator;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state = EventState.PENDING;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Embedded
    Location location;
}


