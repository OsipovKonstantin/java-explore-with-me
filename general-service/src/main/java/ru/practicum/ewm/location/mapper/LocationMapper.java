package ru.practicum.ewm.location.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.entity.Location;

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
