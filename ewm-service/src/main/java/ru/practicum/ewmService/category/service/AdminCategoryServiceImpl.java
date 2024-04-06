package ru.practicum.ewmService.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.category.Category;
import ru.practicum.ewmService.category.dto.CategoryDto;
import ru.practicum.ewmService.category.dto.mapper.CategoryDtoMapper;
import ru.practicum.ewmService.category.storage.AdminCategoryRepository;
import ru.practicum.ewmService.user.exception.ObjectNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final AdminCategoryRepository adminCategoryRepository;
    private final CategoryDtoMapper categoryDtoMapper;

    @Override
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = categoryDtoMapper.toCategory(categoryDto);
        log.info("category = {}", category);
        return categoryDtoMapper.toCategoryDto(adminCategoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        adminCategoryRepository.findById(catId).orElseThrow(() -> {
            log.error("Category with id {} not found", catId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Category with id=%d was not found", catId));
        });
        //проверка, что с категорией не связано ни одно событие
        //делаем запрос в поиск событий по категории //todo
        adminCategoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto, Long catId) {
        Category category = adminCategoryRepository.findById(catId).orElseThrow(() -> {
            log.error("Category with id {} not found", catId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Category with id=%d was not found", catId));
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
