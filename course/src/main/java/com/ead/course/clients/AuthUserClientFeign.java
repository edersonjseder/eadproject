package com.ead.course.clients;

import com.ead.course.clients.params.CourseClientParams;
import com.ead.course.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(value = "${ead.auth-user.name}", url = "${ead.auth-user.url}")
public interface AuthUserClientFeign {
    @GetMapping(path = "/auth_user/users/all")
    Page<UserDto> getUsersByCourse(@SpringQueryMap CourseClientParams params);

    @GetMapping(path = "/auth_user/users/{id}")
    UserDto getUserById(@PathVariable("id") UUID id);
}
