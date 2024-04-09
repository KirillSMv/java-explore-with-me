package ru.practicum.ewmService.compilation.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmService.compilation.Compilation;
import ru.practicum.ewmService.compilation.dto.CompilationDto;
import ru.practicum.ewmService.compilation.dto.NewCompilationDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.mapper.EventDtoMapper;

import java.util.HashSet;
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
                new HashSet<>(eventShortDtoList), compilation.isPinned(), compilation.getTitle());
    }

    public List<CompilationDto> toCompilationDtoList(List<Compilation> compilations, ) {

        compilations.stream().map(comp -> {
            eventDtoMapper.toEventShortDtoList(comp.getEvents())
            toCompilationDto(comp)
        }).collect(Collectors.toList());
    }

    public void test(Map<Long, Compilation> compilationMap, Map<Long, EventShortDto> eventShortDtoMap, Map<Long, List<Long>> eventsIdMap) {

        for (Long list : eventsIdMap.values()) {

        }
    }
}
