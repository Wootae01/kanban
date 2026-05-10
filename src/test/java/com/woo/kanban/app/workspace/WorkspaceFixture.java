package com.woo.kanban.app.workspace;

public class WorkspaceFixture {

    public static final Long ID = 1L;
    public static final Long USER_ID = 1L;

    public static Workspace workspace() {
        return new Workspace(ID, "name", null, USER_ID);
    }

    public static WorkspaceMember adminMember() {
        return new WorkspaceMember(1L, ID, USER_ID, MemberRole.ADMIN);
    }

    public static WorkspaceMember nonAdminMember() {
        return new WorkspaceMember(1L, ID, USER_ID, MemberRole.MEMBER);
    }
}
