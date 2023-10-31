package ru.practicum.ewm.category.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.entity.Category;

@Component
public class CategoryMapper {
    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto()
                .setId(category.getId())
                .setName(category.getName());
    }

    public Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category()
                .setName(newCategoryDto.getName());
    }

    public Category toCategory(Long id, CategoryDto categoryDto) {
        return new Category()
                .setId(id)
                .setName(categoryDto.getName());
    }
}
