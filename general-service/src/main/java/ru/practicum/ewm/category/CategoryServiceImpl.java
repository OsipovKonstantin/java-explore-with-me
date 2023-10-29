package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.entity.Category;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.customclasses.OffsetBasedPageRequest;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class CategoryServiceImpl implements CategoryService {
    private final AdminCategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto save(NewCategoryDto newCategoryDto) {
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    public void delete(Long id) {
        checkExists(id);
        if (eventRepository.existsByCategoryId(id))
            throw new ConflictException("Сушествуют события с удаляемой категорией, поэтому удаление невозможно.");
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        checkExists(id);
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(id, categoryDto)));
    }

    @Transactional(readOnly = true)
    @Override
    public Category findById(Long id) {
        checkExists(id);
        return categoryRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> findAll(Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size);
        return categoryRepository.findAll(page).stream().map(categoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto findDtoById(Long id) {
        return categoryMapper.toCategoryDto(findById(id));
    }

    private void checkExists(Long id) {
        if (!categoryRepository.existsById(id))
            throw new NotFoundException(String.format("Категория с id %d не найдена.", id));
    }
}
