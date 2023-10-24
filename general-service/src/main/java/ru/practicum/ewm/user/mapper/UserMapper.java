package ru.practicum.ewm.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.entity.User;

@UtilityClass
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId())
                .setName(user.getName())
                .setEmail(user.getEmail());
    }

    public static User toUser(NewUserRequest newUserRequest) {
        return new User()
                .setName(newUserRequest.getName())
                .setEmail(newUserRequest.getEmail());
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto()
                .setId(user.getId())
                .setName(user.getName());
    }
}
