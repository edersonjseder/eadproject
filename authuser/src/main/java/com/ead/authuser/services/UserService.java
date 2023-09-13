package com.ead.authuser.services;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.exceptions.UserException;
import com.ead.authuser.exceptions.UserNotFoundException;
import com.ead.authuser.models.User;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.responses.ImageResponse;
import com.ead.authuser.responses.PasswordResponse;
import com.ead.authuser.utils.UserUtils;
import com.ead.authuser.utils.ValidaCPF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static com.ead.authuser.constants.UserMessagesConstants.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserUtils userUtils;

    public Page<UserDto> findAllUsers(Specification<User> spec, Pageable pageable) {
        return userUtils.toListUserDto(userRepository.findAll(spec, pageable));
    }

    public User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public UserDto saveUser(UserDto userDto) {
        User user;

        if (userDto.getId() == null) {
            if (!ValidaCPF.isCPF(userDto.getCpf())) {
                throw new UserException(CPF_MENSAGEM + userDto.getCpf());
            }
            if (userRepository.existsByCpf(userDto.getCpf())) {
                throw new UserException(CPF_EXISTENTE_MENSAGEM + userDto.getCpf());
            }
            if (userRepository.existsByUsername(userDto.getUsername())) {
                throw new UserException(USUARIO_USERNAME_EXISTENTE_MENSAGEM + userDto.getUsername());
            }
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new UserException(USUARIO_EMAIL_EXISTENTE_MENSAGEM + userDto.getEmail());
            }

            user = new User();

            BeanUtils.copyProperties(userDto, user);

            user.setUserStatus(UserStatus.ACTIVE);
            user.setUserType(UserType.STUDENT);
            user.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
            user.setCurrentPasswordDate(LocalDateTime.now(ZoneId.of("UTC")));

        } else {
            user = userRepository.findById(userDto.getId()).orElseThrow(() -> new UserNotFoundException(userDto.getId()));

            user.setFullName(userDto.getFullName());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setUserStatus(UserStatus.valueOf(userDto.getStatus()));
            user.setUserType(UserType.valueOf(userDto.getType()));
        }

        user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return userUtils.toUserDto(userRepository.save(user));
    }

    @Transactional
    public PasswordResponse updatePassword(UserDto userDto) {
        var user = userRepository.findById(userDto.getId()).orElseThrow(() -> new UserNotFoundException(userDto.getId()));

        if (!user.getPassword().equals(userDto.getOldPassword())) {
            throw new UserException(USUARIO_SENHA_MENSAGEM);
        }

        user.setPassword(user.getPassword());
        user.setCurrentPasswordDate(LocalDateTime.now(ZoneId.of("UTC")));
        user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return userUtils.toPasswordResponse(userRepository.save(user));
    }

    @Transactional
    public ImageResponse updateImage(UserDto userDto) {
        var user = userRepository.findById(userDto.getId()).orElseThrow(() -> new UserNotFoundException(userDto.getId()));

        user.setImageUrl(userDto.getImageUrl());
        user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return userUtils.toImageResponse(userRepository.save(user));
    }

    public void deleteUser(UUID id) {
        var user = findUserById(id);
        userRepository.delete(user);
    }
}
