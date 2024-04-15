package ru.practicum.ewmService.request.model;

import lombok.*;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.request.enums.RequestState;
import ru.practicum.ewmService.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
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


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        ParticipationRequest other = (ParticipationRequest) obj;
        return this.id != null && id.equals(other.getId());
    }

    @Override
    public String toString() {
        return "ParticipationRequest{" +
                "id=" + id +
                ", event=" + event +
                ", created=" + created +
                ", requester=" + requester +
                ", status=" + status +
                '}';
    }

    @Override
    public int hashCode() {
        return 12;
    }
}
