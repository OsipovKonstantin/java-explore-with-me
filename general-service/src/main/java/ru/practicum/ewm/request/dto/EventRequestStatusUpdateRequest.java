package ru.practicum.ewm.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.practicum.ewm.request.entity.RequestStatus;

import java.util.Set;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EventRequestStatusUpdateRequest {
    private Set<Long> requestIds;
    private RequestStatus status;
}
