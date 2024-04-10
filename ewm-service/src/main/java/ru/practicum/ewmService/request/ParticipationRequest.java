package ru.practicum.ewmService.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.ewmService.event.Event;
import ru.practicum.ewmService.request.enums.RequestState;
import ru.practicum.ewmService.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @ToString.Exclude
    private Event event;

    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User requester;

    @Enumerated(EnumType.STRING)
    private RequestState status = RequestState.PENDING;
}
