package com.woo.kanban.app.user.service;

import com.woo.kanban.app.user.User;
import com.woo.kanban.app.user.dto.UserCreateRequest;
import com.woo.kanban.app.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
    public void signUp(UserCreateRequest request) {

        // 이미 있는 계정이면 에러 던짐
        userMapper.findByEmail(request.email())
                .ifPresent(u -> { throw new IllegalArgumentException("이미 사용중인 이메일입니다."); });

        // user 삽입
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        userMapper.insert(user);
    }

    // 회원 탈퇴
    public void withdraw(Long id) {
        userMapper.delete(id);
    }
}
