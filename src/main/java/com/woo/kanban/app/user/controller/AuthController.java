package com.woo.kanban.app.user.controller;

import com.woo.kanban.app.user.dto.UserCreateRequest;
import com.woo.kanban.app.user.service.UserService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody @Valid UserCreateRequest request) {
        userService.signUp(request);
        return ResponseEntity.status(200).build();
    }

}
