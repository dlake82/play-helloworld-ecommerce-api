package com.saysimple.users.services;

import com.saysimple.users.dto.UserDto;
import com.saysimple.users.jpa.UserEntity;
import com.saysimple.users.vo.ResponseUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends UserDetailsService {
    UserDto create(UserDto userDto);

    ResponseUser getUserByUserId(String userId);

    UserDto getUserDetailsByEmail(String email);

    Iterable<UserEntity> getUserByAll();

}
