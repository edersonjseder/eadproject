package com.ead.course.services;

import com.ead.course.repositories.CourseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseUserService {
    private final CourseUserRepository courseUserRepository;
}
