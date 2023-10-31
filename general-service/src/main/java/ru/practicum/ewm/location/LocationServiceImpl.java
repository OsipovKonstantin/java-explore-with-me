package ru.practicum.ewm.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.entity.Location;
import ru.practicum.ewm.location.mapper.LocationMapper;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public Location save(LocationDto location) {
        return locationRepository.save(locationMapper.toLocation(location));
    }
}
