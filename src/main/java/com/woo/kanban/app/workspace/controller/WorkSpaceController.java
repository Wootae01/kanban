package com.woo.kanban.app.workspace.controller;

import com.woo.kanban.app.user.CustomUserDetails;
import com.woo.kanban.app.workspace.dto.*;
import com.woo.kanban.app.workspace.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces")
public class WorkSpaceController {

    private final WorkspaceService workspaceService;

    // workspace 생성
    @PostMapping
    public ResponseEntity<Void> createWorkspace(@Valid @RequestBody WorkspaceCreateRequest dto,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        workspaceService.create(dto, userDetails.getId());
        return ResponseEntity.status(201).build();
    }

    // 내 workspace 조회
    @GetMapping
    public ResponseEntity<List<WorkspaceResponse>> getMyWorkspaces(@AuthenticationPrincipal CustomUserDetails userDetails) {

        List<WorkspaceResponse> list = workspaceService.findWorkspaceList(userDetails.getId());
        return ResponseEntity.ok(list);
    }

    // workspace 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceDetailResponse> getWorkspace(@PathVariable Long id) {
        WorkspaceDetailResponse response = workspaceService.findById(id);
        return ResponseEntity.ok(response);
    }

    // workspace 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateWorkspace(@PathVariable Long id,
                                            @AuthenticationPrincipal CustomUserDetails userDetails,
                                            @Valid @RequestBody WorkspaceUpdateRequest dto) {

        workspaceService.update(id, userDetails.getId(), dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long id,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        workspaceService.delete(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    // workspace 멤버 조회
    @GetMapping("/{id}/members")
    public ResponseEntity<List<MemberResponse>> getMemberList(@PathVariable Long id,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<MemberResponse> result = workspaceService.getMemberList(id, userDetails.getId());

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}/members/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id,
                                             @PathVariable Long memberId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        workspaceService.deleteMember(id, memberId, userDetails.getId());
        return ResponseEntity.ok().build();
    }

}
