package ru.practicum.ewmService.event.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmService.category.Category;
import ru.practicum.ewmService.event.Event;
import ru.practicum.ewmService.user.User;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> , QuerydslPredicateExecutor<Event> {

    List<Event> findAllByInitiator(User user, Pageable pageable);


    Integer countByCategory(Category category);


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