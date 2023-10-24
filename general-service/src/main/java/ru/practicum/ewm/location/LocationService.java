package ru.practicum.ewm.location;

import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.entity.Location;

public interface LocationService {
    Location save(LocationDto location);
}
