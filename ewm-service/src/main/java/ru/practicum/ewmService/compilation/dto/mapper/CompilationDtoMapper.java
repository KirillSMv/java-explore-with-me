package ru.practicum.ewmService.compilation.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmService.compilation.dto.CompilationDto;
import ru.practicum.ewmService.compilation.dto.NewCompilationDto;
import ru.practicum.ewmService.compilation.model.Compilation;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.mapper.EventDtoMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationDtoMapper {
    private final EventDtoMapper eventDtoMapper;


    public Compilation toCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(newCompilationDto.isPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }

    public CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> eventShortDtoList) {
        return new CompilationDto(compilation.getId(),
                new ArrayList<>(eventShortDtoList), compilation.isPinned(), compilation.getTitle());
    }

    public List<CompilationDto> toCompilationDtoList(List<Compilation> compilations, Map<Long, EventShortDto> eventShortDtoMap, Map<Long, List<Long>> eventsIdMap) {
        List<CompilationDto> compilationDtoList = compilations.stream().map(this::toCompilationDtoWithEmptyEvents).collect(Collectors.toList());
        return compilationDtoList.stream()
                .peek(comp -> comp.setEvents(getEventShortDtos(comp.getId(), eventsIdMap, eventShortDtoMap))).collect(Collectors.toList());
    }

    private List<EventShortDto> getEventShortDtos(Long compId,
                                                  Map<Long, List<Long>> compIdToEventIds,
                                                  Map<Long, EventShortDto> eventIdToEventDto) {
        List<Long> eventIds = compIdToEventIds.get(compId);
        List<EventShortDto> dtos = new ArrayList<>();
        for (Long id : eventIds) {
            dtos.add(eventIdToEventDto.get(id));
        }
        return dtos;
    }

    private CompilationDto toCompilationDtoWithEmptyEvents(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.isPinned());
        return compilationDto;
    }
}
