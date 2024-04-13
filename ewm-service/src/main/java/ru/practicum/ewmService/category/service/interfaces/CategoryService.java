package ru.practicum.ewmService.category.service.interfaces;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewmService.category.dto.CategoryDto;

import java.util.List;


public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(CategoryDto categoryDto, Long catId);

    List<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategoryById(Long catId);
}
