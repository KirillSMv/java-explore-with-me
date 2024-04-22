package ru.practicum.ewmService.event.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmService.category.model.Category;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.location.model.Location;
import ru.practicum.ewmService.user.model.User;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    List<Event> findAllByInitiator(User user, Pageable pageable);

    Integer countByCategory(Category category);

    List<Event> findAllByLocation(Location location);
}