package ru.practicum.ewmService.user.dto.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.user.dto.NewUserRequest;
import ru.practicum.ewmService.user.dto.UserDto;
import ru.practicum.ewmService.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDtoMapper {

    public User toUser(NewUserRequest userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public List<UserDto> toUserDtoList(List<User> users) {
        return users.stream().map(this::toUserDto).collect(Collectors.toList());
    }

    public EventFullDto.UserShortDto toUserShortDto(User user) {
        return new EventFullDto.UserShortDto(user.getId(), user.getName());
    }
}
