package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.responses.ImageResponse;
import com.ead.authuser.responses.PasswordResponse;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.specs.UserSpec;
import com.ead.authuser.utils.UserUtils;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.ead.authuser.constants.UserMessagesConstants.USUARIO_REMOVIDO_SUCESSO_MENSAGEM;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserUtils userUtils;

    @GetMapping("/all")
    public ResponseEntity<Page<UserDto>> getAllUsers(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, UserSpec spec) {
        var userPage = userService.findAllUsers(spec, pageable);

        // Hateoas link creation snippet
        if (!userPage.isEmpty()) {
            userPage.toList().forEach((user) -> {
                user.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
            });
        }

        return ResponseEntity.ok(userPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.ok(userUtils.toUserDto(userService.findUserById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeUserById(@PathVariable(value = "id") UUID id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(USUARIO_REMOVIDO_SUCESSO_MENSAGEM, HttpStatus.OK);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody @Validated(UserDto.UserView.UserPut.class) @JsonView(UserDto.UserView.UserPut.class) UserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.saveUser(userDto));
    }

    @PutMapping(value = "/password")
    public ResponseEntity<PasswordResponse> updatePassword(@RequestBody @Validated(UserDto.UserView.PasswordPut.class) @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updatePassword(userDto));
    }

    @PutMapping(value = "/image")
    public ResponseEntity<ImageResponse> updateImage(@RequestBody @Validated(UserDto.UserView.ImagePut.class) @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateImage(userDto));
    }
}
