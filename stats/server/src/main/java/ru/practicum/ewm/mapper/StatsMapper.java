package ru.practicum.ewm.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.entity.Hit;

@UtilityClass
public class StatsMapper {
    public Hit toHit(EndpointHit endpointHit) {
        return new Hit()
                .setApp(endpointHit.getApp())
                .setUri(endpointHit.getUri())
                .setIp(endpointHit.getIp())
                .setTimestamp(endpointHit.getTimestamp());
    }
}
