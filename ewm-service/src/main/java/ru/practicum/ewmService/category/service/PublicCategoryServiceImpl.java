package ru.practicum.ewmService.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.category.Category;
import ru.practicum.ewmService.category.dto.CategoryDto;
import ru.practicum.ewmService.category.dto.mapper.CategoryDtoMapper;
import ru.practicum.ewmService.category.storage.PublicCategoryRepository;
import ru.practicum.ewmService.user.exception.ObjectNotFoundException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCategoryServiceImpl implements PublicCategoryService {

    private final PublicCategoryRepository publicCategoryRepository;
    private final CategoryDtoMapper categoryDtoMapper;


    @Override
    public List<CategoryDto> getCategories(Pageable pageable) {
        List<Category> categories = publicCategoryRepository.findAllPageable(pageable);
        log.info("categories = {}", categories);
        return categoryDtoMapper.toCategoryDtoList(categories);
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        Category savedCategory = publicCategoryRepository.findById(catId).orElseThrow(() -> {
            log.error("Category with id {} not found", catId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Category with id=%d was not found", catId));
        });
        return categoryDtoMapper.toCategoryDto(savedCategory);
    }
}
