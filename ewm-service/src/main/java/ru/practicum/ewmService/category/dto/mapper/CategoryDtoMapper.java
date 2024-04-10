package ru.practicum.ewmService.category.dto.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmService.category.dto.CategoryDto;
import ru.practicum.ewmService.category.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryDtoMapper {

    public Category toCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public List<CategoryDto> toCategoryDtoList(List<Category> categories) {
        return categories.stream().map(this::toCategoryDto).collect(Collectors.toList());
    }

}
