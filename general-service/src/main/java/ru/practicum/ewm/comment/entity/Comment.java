package ru.practicum.ewm.comment.entity;

import lombok.*;
import lombok.experimental.Accessors;
import ru.practicum.ewm.comment.dto.CommentState;
import ru.practicum.ewm.event.entity.Event;
import ru.practicum.ewm.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@Accessors(chain = true)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "comment_state")
    private CommentState state;
}
