package ru.practicum.ewm.request.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.practicum.ewm.event.entity.Event;
import ru.practicum.ewm.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participation_requests")
@Getter
@Setter
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User requester;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    private RequestStatus status;

    private LocalDateTime created;
}
