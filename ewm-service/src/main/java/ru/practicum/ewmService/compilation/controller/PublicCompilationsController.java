package ru.practicum.ewmService.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.compilation.dto.CompilationDto;
import ru.practicum.ewmService.compilation.service.interfaces.CompilationService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class PublicCompilationsController {

    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(@RequestParam(name = "pinned", defaultValue = "false") boolean pinned,
                                                                @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                                @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("getCompilations method, parameters: pinned = {}, from = {}, size = {}", pinned, from, size);
        return new ResponseEntity<>(compilationService.getCompilations(pinned, PageRequest.of(from / size, size)), HttpStatus.OK);
    }


    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilationById(@PathVariable(name = "compId") Long compId) {
        log.info("getCompilationById method, parameters: compId = {}", compId);
        return new ResponseEntity<CompilationDto>(compilationService.getCompilationById(compId), HttpStatus.OK);
    }
}

