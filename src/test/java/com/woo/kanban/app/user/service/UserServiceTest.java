package com.woo.kanban.app.user.service;

import com.woo.kanban.app.user.User;
import com.woo.kanban.app.user.dto.UserCreateRequest;
import com.woo.kanban.app.user.mapper.UserMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입_성공")
    void signupSuccess() {

        // given
        UserCreateRequest request = new UserCreateRequest("test@test.com", "password1234", "password1234", "홍길동");
        when(userMapper.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

        // when
        userService.signUp(request);

        // then
        verify(passwordEncoder).encode("password1234");
        verify(userMapper).insert(argThat(user ->
                user.getPassword().equals("encodedPassword")
        ));

    }

    @Test
    @DisplayName("회원가입_실패_중복 이메일")
    void signUpFail() {
        // given
        UserCreateRequest request = new UserCreateRequest("test@test.com", "password1234", "password1234", "홍길동");
        when(userMapper.findByEmail("test@test.com")).thenReturn(Optional.of(new User()));

        // when & then
        Assertions.assertThatThrownBy(() -> userService.signUp(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("회원탈퇴_성공")
    void withdraw() {

        // when
        userService.withdraw(1L);

        // then
        verify(userMapper).delete(1L);
    }
}