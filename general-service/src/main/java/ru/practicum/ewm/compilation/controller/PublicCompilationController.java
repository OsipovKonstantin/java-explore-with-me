package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.CompilationService;
import ru.practicum.ewm.compilation.dto.CompilationDto;

import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.ewm.constants.Constants.MIN_PAGE_FROM;
import static ru.practicum.ewm.constants.Constants.MIN_PAGE_SIZE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> find(@RequestParam(required = false) Boolean pinned,
                                     @Min(MIN_PAGE_FROM) @RequestParam(defaultValue = "0") Integer from,
                                     @Min(MIN_PAGE_SIZE) @RequestParam(defaultValue = "10")
                                     Integer size) {
        return compilationService.find(pinned, from, size);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto findDtoById(@PathVariable(name = "compId") Long id) {
        return compilationService.findDtoById(id);
    }
}
