package com.ead.authuser.utils;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.User;
import com.ead.authuser.responses.ImageResponse;
import com.ead.authuser.responses.PasswordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.ead.authuser.constants.UserMessagesConstants.USUARIO_IMAGE_SUCESSO_MENSAGEM;
import static com.ead.authuser.constants.UserMessagesConstants.USUARIO_SENHA_SUCESSO_MENSAGEM;

@Component
@RequiredArgsConstructor
public class UserUtils {
    private final DateUtils dateUtils;
    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .cpf(user.getCpf())
                .imageUrl(user.getImageUrl())
                .status(user.getUserStatus().name())
                .creationDate(dateUtils.parseDate(user.getCreationDate()))
                .lastUpdateDate(dateUtils.parseDate(user.getLastUpdateDate()))
                .currentPasswordDate(dateUtils.parseDate(user.getCurrentPasswordDate()))
                .build();
    }

    public PasswordResponse toPasswordResponse(User user) {
        return PasswordResponse.builder()
                .message(USUARIO_SENHA_SUCESSO_MENSAGEM)
                .currentPasswordDate(dateUtils.parseDate(user.getCurrentPasswordDate()))
                .lastUpdatedDate(dateUtils.parseDate(user.getLastUpdateDate()))
                .build();
    }

    public ImageResponse toImageResponse(User user) {
        return ImageResponse.builder()
                .message(USUARIO_IMAGE_SUCESSO_MENSAGEM)
                .imageUrl(user.getImageUrl())
                .lastUpdatedDate(dateUtils.parseDate(user.getLastUpdateDate()))
                .build();
    }

    public Page<UserDto> toListUserDto(Page<User> userList) {
        return userList.map(user -> UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .cpf(user.getCpf())
                .imageUrl(user.getImageUrl())
                .status(user.getUserStatus().name())
                .creationDate(dateUtils.parseDate(user.getCreationDate()))
                .lastUpdateDate(dateUtils.parseDate(user.getLastUpdateDate()))
                .currentPasswordDate(dateUtils.parseDate(user.getCurrentPasswordDate()))
                .build());
    }
}
