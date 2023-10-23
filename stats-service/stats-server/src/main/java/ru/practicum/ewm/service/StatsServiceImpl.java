package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.exception.StartAfterEndException;
import ru.practicum.ewm.mapper.StatsMapper;
import ru.practicum.ewm.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public void save(EndpointHit endpointHit) {
        statsRepository.save(StatsMapper.toHit(endpointHit));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStats> findByStartAndEndAndUrisAndIsUniqueIp(LocalDateTime start, LocalDateTime end,
                                                                 List<String> uris, boolean unique) {
        if (start.isAfter(end))
            throw new StartAfterEndException("Время начала должно быть раньше времени окончания.");
        return statsRepository.findByStartAndEndAndUrisAndIsUniqueIp(start, end, uris, unique);
    }

    @Override
    public Long countByUri(String uri) {
        return statsRepository.countByUri(uri);
    }
}
