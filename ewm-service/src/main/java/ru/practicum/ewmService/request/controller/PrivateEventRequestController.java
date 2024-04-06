package ru.practicum.ewmService.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.request.dto.ParticipationRequestDto;
import ru.practicum.ewmService.user.exception.CustomValidationException;

import javax.validation.constraints.Min;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/requests")
public class PrivateEventRequestController {


    @PostMapping
    public ResponseEntity<ParticipationRequestDto> addParticipationRequest(@RequestBody @Validated ParticipationRequestDto participationRequestDto,
                                                                          @PathVariable("userId") @Min(1) Long userId,
                                                                          @RequestParam("eventId") @Min(1) Long eventId,
                                                                           BindingResult errors) {
        checkErrors(errors);
        log.info("addParticipationRequest method, participationRequestDto = {}, userId = {}, eventId = {}", participationRequestDto,
                userId,
                eventId);
        return new ResponseEntity<>(null, HttpStatus.CREATED);

    }

    /*
    нельзя добавить повторный запрос (Ожидается код ошибки 409)
инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)
если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
     */

    private void checkErrors(BindingResult errors) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    throw new CustomValidationException("Error:" + fieldError.getField() + error.getDefaultMessage(), "Incorrectly made request.");
                }
                throw new CustomValidationException(error.getDefaultMessage(), "Incorrectly made request.");
            }
        }
    }
}
