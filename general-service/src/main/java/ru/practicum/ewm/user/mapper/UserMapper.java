package ru.practicum.ewm.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.entity.User;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(user.getId())
                .setName(user.getName())
                .setEmail(user.getEmail());
    }

    public User toUser(NewUserRequest newUserRequest) {
        return new User()
                .setName(newUserRequest.getName())
                .setEmail(newUserRequest.getEmail());
    }

    public UserShortDto toUserShortDto(User user) {
        return new UserShortDto()
                .setId(user.getId())
                .setName(user.getName());
    }
}
