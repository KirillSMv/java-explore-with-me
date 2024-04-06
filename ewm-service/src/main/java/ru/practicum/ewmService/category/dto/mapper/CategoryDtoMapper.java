package ru.practicum.ewmService.category.dto.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmService.category.Category;
import ru.practicum.ewmService.category.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;

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
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (Category category : categories) {
            categoryDtoList.add(toCategoryDto(category));
        }
        return categoryDtoList;
    }

}
