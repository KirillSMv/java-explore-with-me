package ru.practicum.ewmService.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.compilation.Compilation;
import ru.practicum.ewmService.compilation.dto.CompilationDto;
import ru.practicum.ewmService.compilation.dto.mapper.CompilationDtoMapper;
import ru.practicum.ewmService.compilation.service.interfaces.PublicCompilationsService;
import ru.practicum.ewmService.compilation.storage.CompilationRepository;
import ru.practicum.ewmService.event.Event;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.service.interfaces.PrivateEventService;

import java.util.ArrayList;
import java.util.Collection;
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
    public CompilationDto getCompilations(boolean pinnedOnly, Pageable pageable) {
        List<Compilation> compilations;
        if (pinnedOnly) {
            compilations = compilationRepository.findAllByPinned(true, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable).getContent();
        }
        log.info("compilations = {}", compilations);


        List<Long> compilationsId = compilations.stream().map(Compilation::getId).collect(Collectors.toList());

/*        List<Event> events = compilations.stream()
                .map(Compilation::getEvents)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());*/
        List<Event> events = new ArrayList<>();
        for (Compilation compilation : compilations) {
            events.add(compilation.getEvents());
        }

        log.info("eventsId = {}", events);

        List<EventShortDto> eventShortDtoList = privateEventService.getEventShortDtoListWithStatistic(events);
        Map<Long, List<EventShortDto>> eventIdToEventDto = eventShortDtoList.stream().collect(Collectors.groupingBy(EventShortDto::getId));

        Map<Long, List<Long>> compIdToEventIds = compilations.stream().collect(Collectors.groupingBy(Compilation::);


        Map<Long, List<Long>> compIdToEventIds

        List<Event> q

    }


        return compilationDtoMapper.toCompilationDto(compilations);


        return null;
}

    @Override
    public CompilationDto getCompilationById(Long compId) {


        return null;
    }
}
