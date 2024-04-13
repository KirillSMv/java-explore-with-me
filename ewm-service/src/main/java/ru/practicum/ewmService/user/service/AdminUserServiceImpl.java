package ru.practicum.ewmService.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.exceptions.ObjectNotFoundException;
import ru.practicum.ewmService.user.dto.NewUserRequest;
import ru.practicum.ewmService.user.dto.UserDto;
import ru.practicum.ewmService.user.dto.mapper.UserDtoMapper;
import ru.practicum.ewmService.user.model.User;
import ru.practicum.ewmService.user.service.interfaces.AdminUserService;
import ru.practicum.ewmService.user.storage.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    @Transactional
    @Override
    public UserDto addUser(NewUserRequest userDto) {
        User savedUser = userRepository.save(userDtoMapper.toUser(userDto));
        return userDtoMapper.toUserDto(savedUser);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Pageable pageable) {
        List<User> users;
        if (ids != null) {
            users = userRepository.findAllByIdIn(ids, pageable);
        } else {
            users = userRepository.findAllPageable(pageable);
        }
        log.debug("users size = {}", users.size());
        return userDtoMapper.toUserDtoList(users);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} not found", userId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("User with id=%d was not found", userId));
        });
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User savedUser = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} not found", userId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("User with id=%d was not found", userId));
        });
        return userDtoMapper.toUserDto(savedUser);
    }
}
