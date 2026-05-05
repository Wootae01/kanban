package com.woo.kanban.app.user.mapper;

import com.woo.kanban.app.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> findByEmail(String email);

    void insert(User user);
    void delete(Long id);
}
