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
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewmService.Constants.TIME_PATTERN;

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
        SearchParametersAdminRequest searchParametersAdminRequest = checkParameters(users, states, categories, rangeStart, rangeEnd, from, size);
        List<EventFullDto> eventFullDtoList = adminEventService.getEventsBySearch(searchParametersAdminRequest);
        return new ResponseEntity<>(eventFullDtoList, HttpStatus.OK);
    }

    private SearchParametersAdminRequest checkParameters(List<Long> users, List<String> states, List<Long> categories,
                                                         String rangeStart, String rangeEnd, Integer from, Integer size) {
        List<EventState> eventStatesList = null;
        if (states != null) {
            eventStatesList = EventState.convertToEventState(states);
        }
        LocalDateTime rangeStartTime = null;
        if (rangeStart != null) {
            rangeStartTime = LocalDateTime.parse(rangeStart, TIME_PATTERN);
        }
        LocalDateTime rangeEndTime = null;
        if (rangeEnd != null) {
            rangeEndTime = LocalDateTime.parse(rangeEnd, TIME_PATTERN);
        }
        if (rangeStartTime != null && rangeEndTime != null) {
            if (rangeEndTime.isBefore(rangeStartTime)) {
                throw new CustomValidationException("Incorrect parameters set",
                        String.format("rangeEnd cannot be earlier than rangeStart. rangeStart = %s, rangeEnd = %s", rangeStart, rangeEnd));
            }
        }
        return new SearchParametersAdminRequest(users, eventStatesList, categories, rangeStartTime, rangeEndTime, PageRequest.of(from / size, size));


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
