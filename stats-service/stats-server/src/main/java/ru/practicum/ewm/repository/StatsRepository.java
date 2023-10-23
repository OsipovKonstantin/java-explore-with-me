package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.entity.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query(value = "select new ru.practicum.ewm.dto.ViewStats(h.app, h.uri, " +
            "case when :unique = true " +
            "then count(distinct (h.ip)) " +
            "else count(h.ip) end as hits) " +
            "from Hit as h " +
            "where h.timestamp between :start and :end " +
            "and (nullif((:uris), null) is null or h.uri in (:uris)) " +
            "group by h.app, h.uri " +
            "order by hits desc")
    List<ViewStats> findByStartAndEndAndUrisAndIsUniqueIp(LocalDateTime start, LocalDateTime end,
                                                          List<String> uris, boolean unique);

    Long countByUri(String uri);
}
