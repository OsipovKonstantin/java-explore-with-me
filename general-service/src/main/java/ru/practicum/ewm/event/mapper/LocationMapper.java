package ru.practicum.ewm.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.event.entity.Location;

@UtilityClass
public class LocationMapper {
    public static Location toLocation(LocationDto locationDto) {
        return new Location()
                .setLat(locationDto.getLat())
                .setLon(locationDto.getLon());
    }

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto()
                .setLat(location.getLat())
                .setLon(location.getLon());
    }
}
