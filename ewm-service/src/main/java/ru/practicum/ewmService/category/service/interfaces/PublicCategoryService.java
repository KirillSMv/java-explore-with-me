package ru.practicum.ewmService.category.service.interfaces;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewmService.category.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {

    List<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategoryById(Long catId);
}
