package ru.practicum.ewmService.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.compilation.dto.CompilationDto;
import ru.practicum.ewmService.compilation.dto.NewCompilationDto;
import ru.practicum.ewmService.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewmService.compilation.service.interfaces.CompilationService;
import ru.practicum.ewmService.exceptions.CustomValidationException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> addCompilation(@RequestBody @Validated NewCompilationDto newCompilationDto,
                                                         BindingResult errors) {
        checkErrors(errors);
        log.info("addCompilation method, newCompilationDto = {}", newCompilationDto);
        return new ResponseEntity<>(compilationService.addCompilation(newCompilationDto), HttpStatus.CREATED);
    }


    @DeleteMapping("/{compId}")
    public ResponseEntity<String> deleteCompilation(@PathVariable("compId") Long compId) {
        log.info("deleteCompilation method, parameters: compId = {}", compId);

        compilationService.deleteCompilation(compId);
        return new ResponseEntity<>("Compilation deleted", HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(@PathVariable("compId") Long compId,
                                                            @RequestBody @Validated UpdateCompilationRequest updateCompilationRequest,
                                                            BindingResult errors) {
        log.info("updateCompilation method, parameters: compId = {}, updateCompilationRequest = {}", compId, updateCompilationRequest);
        checkErrors(errors);
        return new ResponseEntity<>(compilationService.updateCompilation(compId, updateCompilationRequest), HttpStatus.OK);


    }


    private void checkErrors(BindingResult errors) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    throw new CustomValidationException("Error:" + fieldError.getField() + error.getDefaultMessage(),
                            "Incorrectly made request.");
                }
                throw new CustomValidationException(error.getDefaultMessage(), "Incorrectly made request.");
            }
        }
    }
}
