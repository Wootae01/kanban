package com.woo.kanban.app.user.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserCreateRequestTest {

    Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("유효한 요청은 검증 통과")
    void validRequest() {
        UserCreateRequest request = new UserCreateRequest("test@test.com", "Password1!", "Password1!", "홍길동");
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("이메일 형식 오류")
    void invalidEmail() {
        UserCreateRequest request = new UserCreateRequest("invalid-email", "Password1!", "Password1!", "홍길동");
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    @DisplayName("비밀번호 패턴 오류")
    void invalidPassword() {
        UserCreateRequest request = new UserCreateRequest("test@test.com", "password", "password", "홍길동");
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    @DisplayName("비밀번호 불일치")
    void passwordMismatch() {
        UserCreateRequest request = new UserCreateRequest("test@test.com", "Password1!", "Password2!", "홍길동");
        assertThat(request.isPasswordConfirmed()).isFalse();
    }
}