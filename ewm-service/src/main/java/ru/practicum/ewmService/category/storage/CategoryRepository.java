package ru.practicum.ewmService.category.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmService.category.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
