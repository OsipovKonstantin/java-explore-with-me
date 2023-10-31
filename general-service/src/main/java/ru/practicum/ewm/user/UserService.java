package ru.practicum.ewm.user;

import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.entity.User;

import java.util.List;

public interface UserService {
    List<UserDto> findAllByIdIn(List<Long> ids, Integer from, Integer size);

    UserDto save(NewUserRequest newUserRequest);

    void delete(Long userId);

    User findById(Long userId);

    void checkExists(Long id);
}
