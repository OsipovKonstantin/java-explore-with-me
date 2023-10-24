package ru.practicum.ewm.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.practicum.ewm.exception.validation.OnlyConfirmedOrRejected;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;

    @NotNull
    @OnlyConfirmedOrRejected
    private RequestStatus status;
}

