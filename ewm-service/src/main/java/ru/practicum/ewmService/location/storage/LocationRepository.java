package ru.practicum.ewmService.location.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmService.location.model.EventLocation;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<EventLocation, Long> {
    Optional<EventLocation> findByNameContaining(String text);
}
