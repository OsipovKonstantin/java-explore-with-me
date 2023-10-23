package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.entity.Category;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.repository.AdminCategoryRepository;
import ru.practicum.ewm.event.entity.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.CategoryHasEventsException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.OffsetBasedPageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class CategoryServiceImpl implements CategoryService {
    private final AdminCategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto save(NewCategoryDto newCategoryDto) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    public void delete(Long id) {
        findById(id);
        List<Event> events = eventRepository.findByCategoryId(id);
        if (!events.isEmpty())
            throw new CategoryHasEventsException("Сушествуют события с удаляемой категорией, поэтому удаление невозможно.");
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        findById(id);
        return CategoryMapper.toCategoryDto(categoryRepository.save(new Category().setId(id).setName(categoryDto.getName())));
    }

    @Transactional(readOnly = true)
    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Категория с id %d не найдена.", id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> findAll(Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size);
        return categoryRepository.findAll(page).stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto findDtoById(Long id) {
        return CategoryMapper.toCategoryDto(findById(id));
    }
}
