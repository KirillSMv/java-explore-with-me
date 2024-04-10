package ru.practicum.ewmService.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.compilation.dto.CompilationDto;
import ru.practicum.ewmService.compilation.dto.NewCompilationDto;
import ru.practicum.ewmService.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewmService.compilation.dto.mapper.CompilationDtoMapper;
import ru.practicum.ewmService.compilation.model.Compilation;
import ru.practicum.ewmService.compilation.service.interfaces.AdminCompilationsService;
import ru.practicum.ewmService.compilation.storage.CompilationRepository;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.event.service.interfaces.PrivateEventService;
import ru.practicum.ewmService.exceptions.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminCompilationsServiceImpl implements AdminCompilationsService {

    private final CompilationRepository compilationRepository;
    private final CompilationDtoMapper compilationDtoMapper;
    private final PrivateEventService privateEventService;


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
            List<Event> events = privateEventService.finalAllById(new ArrayList<>(newCompilationDto.getEvents()));
            compilation.setEvents(events);
            savedCompilation = compilationRepository.save(compilation);
            eventShortDtoList = privateEventService.getEventShortDtoListWithStatistic(events);
        }
        return compilationDtoMapper.toCompilationDto(savedCompilation, eventShortDtoList);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() -> {
            log.info("Compilation with id {} could not be found", compId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("Compilation with id=%d was not found", compId));
        });
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> {
            log.info("Compilation with id {} could not be found", compId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("Compilation with id=%d was not found", compId));
        });
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = privateEventService.finalAllById(new ArrayList<>(updateCompilationRequest.getEvents()));
            compilation.setEvents(events);
            eventShortDtoList = privateEventService.getEventShortDtoListWithStatistic(events);
        }
        return compilationDtoMapper.toCompilationDto(updateCompilationFields(compilation, updateCompilationRequest), eventShortDtoList);
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
