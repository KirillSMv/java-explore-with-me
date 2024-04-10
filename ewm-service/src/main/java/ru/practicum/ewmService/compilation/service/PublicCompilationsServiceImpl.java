package ru.practicum.ewmService.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.compilation.dto.CompilationDto;
import ru.practicum.ewmService.compilation.dto.mapper.CompilationDtoMapper;
import ru.practicum.ewmService.compilation.model.Compilation;
import ru.practicum.ewmService.compilation.service.interfaces.PublicCompilationsService;
import ru.practicum.ewmService.compilation.storage.CompilationRepository;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.event.service.interfaces.PrivateEventService;
import ru.practicum.ewmService.exceptions.ObjectNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCompilationsServiceImpl implements PublicCompilationsService {
    private final CompilationRepository compilationRepository;
    private final CompilationDtoMapper compilationDtoMapper;
    private final PrivateEventService privateEventService;


    @Override
    public List<CompilationDto> getCompilations(boolean pinnedOnly, Pageable pageable) {
        List<Compilation> compilations;
        if (pinnedOnly) {
            compilations = compilationRepository.findAllByPinned(true, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable).getContent();
        }
        log.info("compilations = {}", compilations);
        List<Event> events = compilations.stream()
                .map(Compilation::getEvents)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        log.info("eventsId = {}", events);

        List<EventShortDto> eventShortDtoList = privateEventService.getEventShortDtoListWithStatistic(events);
        log.info("eventShortDtoList = {}", eventShortDtoList);


        Map<Long, EventShortDto> eventIdToEventDtoMap = eventShortDtoList.stream()
                .collect(Collectors.toMap(EventShortDto::getId, eventShortDto -> eventShortDto));

        Map<Long, List<Long>> compIdToEventIds = new HashMap<>();
        for (Compilation compilation : compilations) {
            List<Long> eventsIds = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList());
            compIdToEventIds.put(compilation.getId(), eventsIds);
        }
        return compilationDtoMapper.toCompilationDtoList(compilations, eventIdToEventDtoMap, compIdToEventIds);
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> {
            log.error("Compilation with id {} not found", compId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("Compilation with id=%d was not found", compId));
        });
        return compilationDtoMapper.toCompilationDto(compilation,
                privateEventService.getEventShortDtoListWithStatistic(compilation.getEvents()));
    }
}
