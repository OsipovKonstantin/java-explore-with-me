package ru.practicum.ewm.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.compilation.entity.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("select c " +
            "from Compilation c " +
            "where (nullif((:pinned), null) is null or c.pinned = :pinned)")
    Page<Compilation> findByPinned(Boolean pinned, Pageable page);
}
