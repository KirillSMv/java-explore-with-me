package ru.practicum.ewmService.category.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmService.category.Category;

public interface AdminCategoryRepository extends JpaRepository<Category, Long> {
}
