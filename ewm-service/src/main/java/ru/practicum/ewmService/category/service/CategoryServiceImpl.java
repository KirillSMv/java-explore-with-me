package ru.practicum.ewmService.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.category.dto.CategoryDto;
import ru.practicum.ewmService.category.dto.mapper.CategoryDtoMapper;
import ru.practicum.ewmService.category.model.Category;
import ru.practicum.ewmService.category.service.interfaces.CategoryService;
import ru.practicum.ewmService.category.storage.CategoryRepository;
import ru.practicum.ewmService.event.storage.EventRepository;
import ru.practicum.ewmService.exceptions.CategoryProcessingException;
import ru.practicum.ewmService.exceptions.ObjectNotFoundException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDtoMapper categoryDtoMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = categoryDtoMapper.toCategory(categoryDto);
        log.debug("category {}", category);
        return categoryDtoMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> {
            log.error("Category with id {} not found", catId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("Category with id=%d was not found", catId));
        });
        if (eventRepository.countByCategory(category) > 0) {
            log.error("The category is not empty");
            throw new CategoryProcessingException("For the requested operation the conditions are not met.",
                    "The category is not empty");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto, Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> {
            log.error("Category with id {} not found", catId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("Category with id=%d was not found", catId));
        });
        category.setName(categoryDto.getName());
        return categoryDtoMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> getCategories(Pageable pageable) {
        List<Category> categories = categoryRepository.findAllPageable(pageable);
        log.debug("categories size = {}", categories.size());
        return categoryDtoMapper.toCategoryDtoList(categories);
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        Category savedCategory = categoryRepository.findById(catId).orElseThrow(() -> {
            log.error("Category with id {} not found", catId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("Category with id=%d was not found", catId));
        });
        return categoryDtoMapper.toCategoryDto(savedCategory);
    }
}
