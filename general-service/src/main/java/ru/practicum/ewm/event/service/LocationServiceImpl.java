package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.event.entity.Location;
import ru.practicum.ewm.event.mapper.LocationMapper;
import ru.practicum.ewm.event.repository.LocationRepository;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public Location save(LocationDto location) {
        return locationRepository.save(LocationMapper.toLocation(location));
    }
}
