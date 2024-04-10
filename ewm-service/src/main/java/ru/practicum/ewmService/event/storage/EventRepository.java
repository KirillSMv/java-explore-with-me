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
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    List<Event> findAllByInitiator(User user, Pageable pageable);


    Integer countByCategory(Category category);
}