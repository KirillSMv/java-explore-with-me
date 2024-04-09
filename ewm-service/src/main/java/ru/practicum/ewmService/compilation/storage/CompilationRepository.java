package ru.practicum.ewmService.compilation.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmService.compilation.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> findAllByPinned(boolean pinned, Pageable pageable);

}
