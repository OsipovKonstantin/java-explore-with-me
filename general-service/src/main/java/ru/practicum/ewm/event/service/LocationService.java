package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.event.entity.Location;

public interface LocationService {
    Location save(LocationDto location);
}
