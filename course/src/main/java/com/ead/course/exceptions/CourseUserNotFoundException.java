package com.ead.course.exceptions;

public class CourseUserNotFoundException extends RuntimeException {
    public CourseUserNotFoundException(String value) {
        super(value);
    }
}
