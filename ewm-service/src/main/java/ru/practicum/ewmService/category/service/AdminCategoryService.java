package ru.practicum.ewmService.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmService.category.controller.AdminCategoryController;
import ru.practicum.ewmService.category.dto.CategoryDto;
import ru.practicum.ewmService.category.storage.AdminCategoryRepository;


public interface AdminCategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(CategoryDto categoryDto, Long catId);
}
