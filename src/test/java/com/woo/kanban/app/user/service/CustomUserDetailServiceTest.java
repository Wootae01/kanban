package com.woo.kanban.app.user.service;

import com.woo.kanban.app.user.User;
import com.woo.kanban.app.user.mapper.UserMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailServiceTest {

    @Mock
    UserMapper userMapper;

    @InjectMocks
    CustomUserDetailService customUserDetailService;

    @Test
    @DisplayName("로그인_성공")
    void loginSuccess() {
        // given
        String email = "test@test.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password1234");
        user.setName("홍길동");
        Mockito.when(userMapper.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        UserDetails userDetails = customUserDetailService.loadUserByUsername(email);

        // then
        Assertions.assertThat(userDetails.getUsername()).isEqualTo(email);
    }

    @Test
    @DisplayName("로그인_실패_이메일_없음")
    void loginFail() {
        // given
        String email = "test@test.com";
        Mockito.when(userMapper.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> customUserDetailService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
