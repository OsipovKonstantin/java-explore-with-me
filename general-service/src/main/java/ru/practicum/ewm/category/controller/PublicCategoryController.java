package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.CategoryService;
import ru.practicum.ewm.category.dto.CategoryDto;

import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.ewm.constants.Constants.MIN_PAGE_FROM;
import static ru.practicum.ewm.constants.Constants.MIN_PAGE_SIZE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> findAll(@Min(MIN_PAGE_FROM) @RequestParam(defaultValue = "0") Integer from,
                                     @Min(MIN_PAGE_SIZE) @RequestParam(defaultValue = "10") Integer size) {
        return categoryService.findAll(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto findById(@PathVariable(name = "catId") Long id) {
        return categoryService.findDtoById(id);
    }
}
