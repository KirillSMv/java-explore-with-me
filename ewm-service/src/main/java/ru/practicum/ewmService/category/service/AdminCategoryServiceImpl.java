package ru.practicum.ewmService.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.category.dto.CategoryDto;
import ru.practicum.ewmService.category.dto.mapper.CategoryDtoMapper;
import ru.practicum.ewmService.category.model.Category;
import ru.practicum.ewmService.category.service.interfaces.AdminCategoryService;
import ru.practicum.ewmService.category.storage.CategoryRepository;
import ru.practicum.ewmService.event.storage.EventRepository;
import ru.practicum.ewmService.exceptions.CategoryProcessingException;
import ru.practicum.ewmService.exceptions.ObjectNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDtoMapper categoryDtoMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = categoryDtoMapper.toCategory(categoryDto);
        log.info("category = {}", category);
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
        /* вот тут хочу уточнить, поскольку сущность category уже отслеживается Hibernate, то в случае сеттера сразу
        делается запрос в БД на обноваление сущности. Нужно ли потом вызывать метод save()?
         Я знаю, что по сути он не нужен, но в ТЗ shareIT у меня потом были проблемы с тестами при использовании мока и
         я дополнительно добавлял этот метод. Вот хочу спросить уже окончательно решить дилемму, как правильнее сделать?
         */
        return categoryDtoMapper.toCategoryDto(category);
    }
}
