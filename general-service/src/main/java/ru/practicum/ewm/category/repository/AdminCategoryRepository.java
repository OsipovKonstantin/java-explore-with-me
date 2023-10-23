package ru.practicum.ewm.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.category.entity.Category;

public interface AdminCategoryRepository extends JpaRepository<Category, Long> {
}
