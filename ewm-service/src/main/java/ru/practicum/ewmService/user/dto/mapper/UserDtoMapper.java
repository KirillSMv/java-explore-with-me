package ru.practicum.ewmService.user.dto.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.user.User;
import ru.practicum.ewmService.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDtoMapper {

    public User toUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public List<UserDto> toUserDtoList(List<User> users) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : users) {
            userDtoList.add(toUserDto(user));
        }
        return userDtoList;
    }

    public EventFullDto.UserShortDto toUserShortDto(User user) {
        return new EventFullDto.UserShortDto(user.getId(), user.getName());
    }
}
