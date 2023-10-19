package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.mapper.StatsMapper;
import ru.practicum.ewm.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public void save(EndpointHit endpointHit) {
        statsRepository.save(StatsMapper.toHit(endpointHit));
    }

    @Override
    public List<ViewStats> findByStartAndEndAndUrisAndIsUniqueIp(LocalDateTime start, LocalDateTime end,
                                                                 List<String> uris, boolean unique) {
        return statsRepository.findByStartAndEndAndUrisAndIsUniqueIp(start, end, uris, unique);
    }
}
