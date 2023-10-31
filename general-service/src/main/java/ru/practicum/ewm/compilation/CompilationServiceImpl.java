package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.entity.Compilation;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.customclasses.OffsetBasedPageRequest;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.entity.Event;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventService eventService;
    private final CompilationMapper compilationMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> find(Boolean pinned, Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size);
        return compilationRepository.findByPinned(pinned, page)
                .stream().map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto findDtoById(Long id) {
        Compilation compilation = findById(id);
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        List<Event> events = eventService.findByIdIn(newCompilationDto.getEvents());
        return compilationMapper.toCompilationDto(compilationRepository
                .save(compilationMapper.toCompilation(newCompilationDto, events)));
    }

    @Override
    public void deleteById(Long id) {
        compilationRepository.deleteById(id);
    }

    @Override
    public CompilationDto update(Long id, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = findById(id);
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation
                .setEvents(updateCompilationRequest.getEvents() == null
                        ? compilation.getEvents()
                        : eventService.findByIdIn(updateCompilationRequest.getEvents()))
                .setPinned(updateCompilationRequest.getPinned() == null
                        ? compilation.getPinned()
                        : updateCompilationRequest.getPinned())
                .setTitle(updateCompilationRequest.getTitle() == null
                        ? compilation.getTitle()
                        : updateCompilationRequest.getTitle())));
    }

    private Compilation findById(Long id) {
        checkExists(id);
        return compilationRepository.findById(id).get();
    }

    private void checkExists(Long id) {
        if (!compilationRepository.existsById(id))
            throw new NotFoundException(String.format("Подборка с id %d не найдена.", id));
    }
}
