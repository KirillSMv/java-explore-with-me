package ru.practicum.ewmService.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.category.dto.CategoryDto;
import ru.practicum.ewmService.category.service.interfaces.AdminCategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Validated
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("addCategory method, categoryDto = {}", categoryDto);
        return new ResponseEntity<>(adminCategoryService.addCategory(categoryDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid CategoryDto categoryDto,
                                                      @PathVariable("catId") @Min(value = 1) Long catId) {
        log.info("deleteCategory method, categoryId = {}", catId);
        return new ResponseEntity<>(adminCategoryService.updateCategory(categoryDto, catId), HttpStatus.OK);
    }


    @DeleteMapping("/{catId}")
    public ResponseEntity<String> deleteCategory(@PathVariable("catId") @Min(value = 1) Long catId) {
        log.info("deleteCategory method, categoryId = {}", catId);
        adminCategoryService.deleteCategory(catId);
        return new ResponseEntity<>("Категория удалена", HttpStatus.NO_CONTENT);
    }
}
