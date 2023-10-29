package ru.practicum.ewm.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.entity.ParticipationRequest;

@Component
public class ParticipationRequestMapper {
    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto()
                .setId(participationRequest.getId())
                .setRequester(participationRequest.getRequester().getId())
                .setEvent(participationRequest.getEvent().getId())
                .setStatus(participationRequest.getStatus())
                .setCreated(participationRequest.getCreated());
    }
}
