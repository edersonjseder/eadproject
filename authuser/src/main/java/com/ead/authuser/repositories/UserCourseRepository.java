package com.ead.authuser.repositories;

import com.ead.authuser.models.User;
import com.ead.authuser.models.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourse, UUID> {
    boolean existsByUserAndCourseId(User user, UUID courseId);
}
