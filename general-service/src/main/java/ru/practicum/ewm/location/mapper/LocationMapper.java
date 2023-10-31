package ru.practicum.ewm.location.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.entity.Location;

@Component
public class LocationMapper {
    public Location toLocation(LocationDto locationDto) {
        return new Location()
                .setLat(locationDto.getLat())
                .setLon(locationDto.getLon());
    }

    public LocationDto toLocationDto(Location location) {
        return new LocationDto()
                .setLat(location.getLat())
                .setLon(location.getLon());
    }
}
