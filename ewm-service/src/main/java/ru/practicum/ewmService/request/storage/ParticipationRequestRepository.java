package ru.practicum.ewmService.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmService.event.Event;
import ru.practicum.ewmService.request.ParticipationRequest;
import ru.practicum.ewmService.user.User;

import java.util.List;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByRequester(User user);

    List<ParticipationRequest> findAllByEvent(Event event);
}
