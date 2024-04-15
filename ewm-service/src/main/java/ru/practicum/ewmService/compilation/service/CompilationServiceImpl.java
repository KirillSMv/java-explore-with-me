package ru.practicum.ewmService.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.compilation.dto.CompilationDto;
import ru.practicum.ewmService.compilation.dto.NewCompilationDto;
import ru.practicum.ewmService.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewmService.compilation.dto.mapper.CompilationDtoMapper;
import ru.practicum.ewmService.compilation.model.Compilation;
import ru.practicum.ewmService.compilation.service.interfaces.CompilationService;
import ru.practicum.ewmService.compilation.storage.CompilationRepository;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.event.service.interfaces.EventService;
import ru.practicum.ewmService.exceptions.ObjectNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationDtoMapper compilationDtoMapper;
    private final EventService eventService;


    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationDtoMapper.toCompilation(newCompilationDto);

        Compilation savedCompilation;
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        if (newCompilationDto.getEvents() == null) {
            compilation.setEvents(new ArrayList<>());
            savedCompilation = compilationRepository.save(compilation);
        } else {
            List<Event> events = eventService.finalAllById(new ArrayList<>(newCompilationDto.getEvents()));
            compilation.setEvents(events);
            savedCompilation = compilationRepository.save(compilation);
            eventShortDtoList = eventService.getEventShortDtoListWithStatistic(events);
        }
        return compilationDtoMapper.toCompilationDto(savedCompilation, eventShortDtoList);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() -> {
            log.error("Compilation with id {} could not be found", compId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("Compilation with id=%d was not found", compId));
        });
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> {
            log.error("Compilation with id {} could not be found", compId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("Compilation with id=%d was not found", compId));
        });
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = eventService.finalAllById(new ArrayList<>(updateCompilationRequest.getEvents()));
            compilation.setEvents(events);
            eventShortDtoList = eventService.getEventShortDtoListWithStatistic(events);
        }
        return compilationDtoMapper.toCompilationDto(updateCompilationFields(compilation, updateCompilationRequest), eventShortDtoList);
    }

    @Override
    public List<CompilationDto> getCompilations(boolean pinnedOnly, Pageable pageable) {
        List<Compilation> compilations;
        if (pinnedOnly) {
            compilations = compilationRepository.findAllByPinned(true, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable).getContent();
        }

        List<Event> events = compilations.stream()
                .map(Compilation::getEvents)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        List<EventShortDto> eventShortDtoList = eventService.getEventShortDtoListWithStatistic(events);
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
                eventService.getEventShortDtoListWithStatistic(compilation.getEvents()));
    }

    private Compilation updateCompilationFields(Compilation compilation, UpdateCompilationRequest updateCompilationRequest) {
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        return compilation;
    }
}
