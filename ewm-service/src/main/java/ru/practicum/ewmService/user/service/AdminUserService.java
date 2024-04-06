package ru.practicum.ewmService.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewmService.user.dto.UserDto;

import java.util.List;

public interface AdminUserService {

    UserDto addUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> ids, Pageable pageable);

    void deleteUser(Long userId);

    UserDto getUserById(Long userId);
}
