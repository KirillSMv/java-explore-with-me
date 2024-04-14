package ru.practicum.ewmService.compilation.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmService.compilation.model.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @EntityGraph(value = "Compilation.events")
    List<Compilation> findAllByPinned(boolean pinned, Pageable pageable);

    @EntityGraph(value = "Compilation.events")
    Page<Compilation> findAll(Pageable pageable);
}
