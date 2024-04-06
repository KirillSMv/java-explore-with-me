package ru.practicum.ewmService.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.category.dto.CategoryDto;
import ru.practicum.ewmService.category.service.PublicCategoryService;
import ru.practicum.ewmService.event.dto.EventFullDto;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class PublicCategoryController {

    private final PublicCategoryService publicCategoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(
            @RequestParam(value = "from", defaultValue = "0") @Min(value = 0) Integer from,
            @RequestParam(value = "size", defaultValue = "10") @Min(value = 1) Integer size) {
        log.info("getCategories method, parameters: from = {}, size = {}", from, size);
        return new ResponseEntity<>(publicCategoryService.getCategories(PageRequest.of(from / size, size)), HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getUser(@PathVariable("catId") @Min(value = 1) Long catId) {
        log.info("getUser method, parameters userId = {}", catId);
        return new ResponseEntity<>(publicCategoryService.getCategoryById(catId), HttpStatus.OK);
    }
}

