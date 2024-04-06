package ru.practicum.ewmService.category.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmService.category.Category;

import java.util.List;

public interface PublicCategoryRepository extends JpaRepository<Category, Long> {

    @Query("select cat from Category cat")
    List<Category> findAllPageable(Pageable pageable);
}
