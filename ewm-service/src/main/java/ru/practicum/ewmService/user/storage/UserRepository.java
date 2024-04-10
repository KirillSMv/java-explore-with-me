package ru.practicum.ewmService.user.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmService.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByIdIn(List<Long> ids, Pageable pageable);

    @Query("select us from User us")
    List<User> findAllPageable(Pageable pageable);
}
