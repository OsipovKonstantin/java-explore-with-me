package ru.practicum.ewm.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.entity.Compilation;
import ru.practicum.ewm.event.entity.Event;
import ru.practicum.ewm.event.mapper.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;

    public Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return new Compilation()
                .setEvents(events)
                .setPinned(newCompilationDto.getPinned())
                .setTitle(newCompilationDto.getTitle());
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto()
                .setId(compilation.getId())
                .setEvents(compilation.getEvents().stream().map(eventMapper::toEventShortDto).collect(Collectors.toSet()))
                .setPinned(compilation.getPinned())
                .setTitle(compilation.getTitle());
    }
}
