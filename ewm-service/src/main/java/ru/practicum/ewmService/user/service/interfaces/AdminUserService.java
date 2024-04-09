package ru.practicum.ewmService.user.service.interfaces;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewmService.user.dto.NewUserRequest;
import ru.practicum.ewmService.user.dto.UserDto;

import java.util.List;

public interface AdminUserService {

    UserDto addUser(NewUserRequest userDto);

    List<UserDto> getUsers(List<Long> ids, Pageable pageable);

    void deleteUser(Long userId);

    UserDto getUserById(Long userId);
}
