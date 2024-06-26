package ru.practicum.ewmService.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.exceptions.CustomValidationException;
import ru.practicum.ewmService.user.dto.NewUserRequest;
import ru.practicum.ewmService.user.dto.UserDto;
import ru.practicum.ewmService.user.service.interfaces.UserService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody @Validated NewUserRequest newUser, BindingResult errors) {
        checkErrors(errors);
        log.info("addUser method, newUser: {}", newUser);
        return new ResponseEntity<>(userService.addUser(newUser), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(value = "ids", required = false) List<Long> ids,
                                                  @RequestParam(value = "from", defaultValue = "0") @Min(value = 0) Integer from,
                                                  @RequestParam(value = "size", defaultValue = "10") @Min(value = 1) Integer size) {
        log.info("getUsers method, parameters: ids = {}, from = {}, size = {}", ids, from, size);
        return new ResponseEntity<>(userService.getUsers(ids, PageRequest.of(from / size, size)), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") @Min(value = 1) Long userId) {
        log.info("getUser method, parameters userId = {}", userId);
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") @Min(value = 1) Long userId) {
        log.info("deleteUser method, userId = {}", userId);
        userService.deleteUser(userId);
        return new ResponseEntity<>("Пользователь удален", HttpStatus.NO_CONTENT);

    }

    private void checkErrors(BindingResult errors) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    throw new CustomValidationException("Error:" + fieldError.getField() + error.getDefaultMessage(),
                            "Incorrectly made request.");
                }
                throw new CustomValidationException(error.getDefaultMessage(), "Incorrectly made request.");
            }
        }
    }
}
