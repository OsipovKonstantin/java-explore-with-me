package ru.practicum.ewm.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.entity.Compilation;
import ru.practicum.ewm.event.entity.Event;

import java.util.List;

@UtilityClass
public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return new Compilation()
                .setEvents(events)
                .setPinned(newCompilationDto.getPinned())
                .setTitle(newCompilationDto.getTitle());
    }
}
