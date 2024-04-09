package ru.practicum.ewmService.compilation.service.interfaces;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmService.compilation.dto.CompilationDto;

public interface PublicCompilationsService {
    CompilationDto getCompilations(boolean pinned, Pageable pageable);

    CompilationDto getCompilationById(Long compId);

}
