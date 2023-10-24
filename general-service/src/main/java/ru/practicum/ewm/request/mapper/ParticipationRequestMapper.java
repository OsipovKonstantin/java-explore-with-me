package ru.practicum.ewm.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.entity.ParticipationRequest;

@UtilityClass
public class ParticipationRequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto()
                .setId(participationRequest.getId())
                .setRequester(participationRequest.getRequester().getId())
                .setEvent(participationRequest.getEvent().getId())
                .setStatus(participationRequest.getStatus())
                .setCreated(participationRequest.getCreated());
    }
}
