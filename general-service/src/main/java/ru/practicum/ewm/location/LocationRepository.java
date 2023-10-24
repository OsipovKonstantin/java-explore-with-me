package ru.practicum.ewm.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.location.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
