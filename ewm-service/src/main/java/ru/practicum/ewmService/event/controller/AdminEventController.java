package ru.practicum.ewmService.event.controller;

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
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.SearchParametersAdminRequest;
import ru.practicum.ewmService.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewmService.event.enums.EventState;
import ru.practicum.ewmService.event.service.interfaces.AdminEventService;
import ru.practicum.ewmService.exceptions.CustomValidationException;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final AdminEventService adminEventService;

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByAdmin(@PathVariable("eventId") Long eventId,
                                                           @RequestBody @Validated UpdateEventAdminRequest updateEventAdminRequest,
                                                           BindingResult errors) {
        log.info("updateEventByAdmin method, parameters: updateEventAdminRequest = {}, eventId = {}", updateEventAdminRequest, eventId);
        checkErrors(errors);
        return new ResponseEntity<>(adminEventService.updateEventByAdmin(updateEventAdminRequest, eventId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEventsByParameters(@RequestParam(name = "users", required = false) List<Long> users,
                                                                    @RequestParam(name = "states", required = false) List<String> states,
                                                                    @RequestParam(name = "categories", required = false) List<Long> categories,
                                                                    @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                                    @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                                    @RequestParam(value = "from", defaultValue = "0") @Min(value = 0) Integer from,
                                                                    @RequestParam(value = "size", defaultValue = "10") @Min(value = 1) Integer size) {

        log.info("getEvents method, parameters: users = {}, states = {}, categories = {}, rangeStart = {}, " +
                        "rangeEnd = {}, from = {}, size = {}", users, states,
                categories, rangeStart, rangeEnd, from, size);
        List<EventState> eventStatesList = EventState.convertToEventState(states);

        List<EventFullDto> eventFullDtoList = adminEventService.getEventsBySearch(new SearchParametersAdminRequest(users, eventStatesList,
                categories, rangeStart, rangeEnd, PageRequest.of(from / size, size)));
        return new ResponseEntity<>(eventFullDtoList, HttpStatus.OK);
    }

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
