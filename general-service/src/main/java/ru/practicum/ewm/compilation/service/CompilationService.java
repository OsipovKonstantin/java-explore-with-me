package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> find(Boolean pinned, Integer from, Integer size);

    CompilationDto findDtoById(Long id);

    CompilationDto save(NewCompilationDto newCompilationDto);

    void deleteById(Long id);

    CompilationDto update(Long id, UpdateCompilationRequest updateCompilationRequest);
}
