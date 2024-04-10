package ru.practicum.ewmService.compilation.service.interfaces;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmService.compilation.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationsService {
    List<CompilationDto> getCompilations(boolean pinned, Pageable pageable);

    CompilationDto getCompilationById(Long compId);

}
