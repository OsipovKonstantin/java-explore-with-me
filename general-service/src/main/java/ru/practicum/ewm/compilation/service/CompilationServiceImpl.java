package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.entity.Compilation;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.entity.Event;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.OffsetBasedPageRequest;
import ru.practicum.ewm.request.entity.RequestStatus;
import ru.practicum.ewm.request.service.ParticipationRequestService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final ParticipationRequestService requestService;
    private final StatsClient statsClient;
    private final EventService eventService;

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> find(Boolean pinned, Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size);
        return compilationRepository.findByPinned(pinned, page)
                .stream().map(this::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto findDtoById(Long id) {
        Compilation compilation = findById(id);
        return toCompilationDto(compilation);
    }

    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        List<Event> events = eventService.findByIdIn(newCompilationDto.getEvents());
        return toCompilationDto(compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto, events)));
    }

    @Override
    public void deleteById(Long id) {
        compilationRepository.deleteById(id);
    }

    @Override
    public CompilationDto update(Long id, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = findById(id);
        return toCompilationDto(compilationRepository.save(compilation
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
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Подборка с id %d не найдена.", id)));
    }

    private CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto()
                .setId(compilation.getId())
                .setEvents(compilation.getEvents().stream().map(e -> EventMapper.toEventShortDto(e,
                        requestService.countByEventIdAndStatus(e.getId(), RequestStatus.CONFIRMED),
                        statsClient.countByEventId(e.getId()))).collect(Collectors.toSet()))
                .setPinned(compilation.getPinned())
                .setTitle(compilation.getTitle());
    }
}
