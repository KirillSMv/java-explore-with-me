package ru.practicum.ewmService.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmService.request.ParticipationRequest;

@Repository
public interface PrivateParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

}
