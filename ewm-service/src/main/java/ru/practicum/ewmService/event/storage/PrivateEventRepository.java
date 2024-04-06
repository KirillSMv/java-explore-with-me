package ru.practicum.ewmService.event.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmService.event.Event;
import ru.practicum.ewmService.event.enums.EventState;
import ru.practicum.ewmService.user.User;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface PrivateEventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiator(User user, Pageable pageable); //todo сортировку добавить

/*    @Query(value = "UPDATE events " +
            "SET annotation = COALESCE(CAST(:annotation AS TEXT), annotation), " +
            "category_id = COALESCE(:category, category_id), " +
            "description = COALESCE(CAST(:description AS TEXT), description), " +
            "event_date = COALESCE(CAST(:eventDate AS date), event_date), " +
            "lat = COALESCE(:lat, lat), " +
            "lon = COALESCE(:lon, lon), " +
            "paid = COALESCE(:paid, paid), " +
            "participant_limit = COALESCE(:participantLimit, participant_limit), " +
            "request_moderation = COALESCE(:requestModeration, request_moderation), " +
            "state = COALESCE(:state, state), " +
            "title = COALESCE(CAST(:title AS TEXT), title) " +
            "WHERE id = :id", nativeQuery = true)
    void updateEventForNotNullFields(@Param("id") Long id, @Param("annotation") String annotation,
                                      @Param("category") Long category, @Param("description") String description,
                                      @Param("eventDate") LocalDateTime eventDate, @Param("lat") Float lat,
                                      @Param("lon") Float lon, @Param("paid") Boolean paid,
                                      @Param("participantLimit") Integer participantLimit,
                                      @Param("requestModeration") Boolean requestModeration,
                                      @Param("state") EventState state, @Param("title") String title);*/
}