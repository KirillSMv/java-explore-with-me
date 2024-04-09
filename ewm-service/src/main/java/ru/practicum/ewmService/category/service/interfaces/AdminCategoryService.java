package ru.practicum.ewmService.category.service.interfaces;

import ru.practicum.ewmService.category.dto.CategoryDto;


public interface AdminCategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(CategoryDto categoryDto, Long catId);
}
