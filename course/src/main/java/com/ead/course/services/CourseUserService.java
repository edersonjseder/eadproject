package com.ead.course.services;

import com.ead.course.clients.AuthUserClientFeign;
import com.ead.course.clients.params.CourseClientParams;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.exceptions.CourseException;
import com.ead.course.exceptions.CourseUserException;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.utils.CourseUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.ead.course.constants.CourseMessagesConstants.COURSE_USER_MENSAGEM;
import static com.ead.course.constants.CourseMessagesConstants.COURSE_USUARIO_CADASTRADO_MENSAGEM;

@Service
@RequiredArgsConstructor
public class CourseUserService {
    private final CourseUserRepository courseUserRepository;
    private final CourseService courseService;
    private final CourseUserUtils courseUserUtils;
    private final AuthUserClientFeign authUserClientFeign;

    public Page<UserDto> getUsersByCourse(UUID courseId, Pageable pageable) {
        return authUserClientFeign.getUsersByCourse(CourseClientParams.builder()
                .courseId(courseId)
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .sort(pageable.getSort().toString().replaceAll(": ", ","))
                .build());
    }

    public SubscriptionDto saveSubscription(UUID courseId, SubscriptionDto subscriptionDto) {
        var course = courseService.getOneCourse(courseId);

        if (courseUserRepository.existsByCourseAndUserId(course, subscriptionDto.getUserId())) {
            throw new CourseException(COURSE_USUARIO_CADASTRADO_MENSAGEM + course.getName());
        }

        var user = authUserClientFeign.getUserById(subscriptionDto.getUserId());

        if (user.getStatus().equals("BLOCKED")) {
            throw new CourseUserException(COURSE_USER_MENSAGEM);
        }

        return courseUserUtils
                .toSubscriptionDto(courseUserRepository.save(courseUserUtils.toCourseUser(subscriptionDto, course)), user);
    }
}
