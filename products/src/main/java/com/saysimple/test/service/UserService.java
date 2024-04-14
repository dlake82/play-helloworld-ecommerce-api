package com.saysimple.users.service;

import com.saysimple.users.dto.UserDto;
import com.saysimple.users.jpa.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto create(UserDto userDto);

    UserDto get(String userId);

    Iterable<UserEntity> list();

    UserDto getByEmail(String userName);
}
