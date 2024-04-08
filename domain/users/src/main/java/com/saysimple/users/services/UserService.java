package com.saysimple.users.services;

import com.saysimple.users.dto.UserDto;
import com.saysimple.users.jpa.UserEntity;
import com.saysimple.users.vo.ResponseUser;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);
    ResponseUser getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();
}
