package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.customclasses.OffsetBasedPageRequest;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.AdminUserRepository;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.entity.User;
import ru.practicum.ewm.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {
    private final AdminUserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAllById(List<Long> ids, Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size);
        if (ids == null || ids.isEmpty())
            return userRepository.findAll(page).stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        else
            return userRepository.findAllByIdIn(ids, page).stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto save(NewUserRequest newUserRequest) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(newUserRequest)));
    }

    @Override
    public void delete(Long userId) {
        checkExists(userId);
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long userId) {
        checkExists(userId);
        return userRepository.findById(userId).get();
    }

    @Override
    public void checkExists(Long id) {
        if (!userRepository.existsById(id))
            throw new NotFoundException(String.format("Пользователь с id %d не найден.", id));
    }
}
