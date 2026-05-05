package com.woo.kanban.app.user.controller;

import com.woo.kanban.app.user.CustomUserDetails;
import com.woo.kanban.app.user.service.UserService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    // 회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<Void> withdraw(@AuthenticationPrincipal CustomUserDetails userDetails, HttpSession session) {
        userService.withdraw(userDetails.getId());
        session.invalidate();
        return ResponseEntity.status(200).build();
    }
}
