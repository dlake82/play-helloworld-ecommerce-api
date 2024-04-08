package com.saysimple.users.services;

import com.saysimple.users.dto.UserDto;
import com.saysimple.users.jpa.UserEntity;
import com.saysimple.users.jpa.UserRepository;
import com.saysimple.users.vo.ResponseOrder;
import com.saysimple.users.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto create(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        ModelMapper mapper = new ModelMapper();

        userDto.setUserId(UUID.randomUUID().toString());
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies
                .STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);

        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    public Iterable<UserEntity> getUserByAll(){
        return userRepository.findAll();
    }

    @Override
    public ResponseUser getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new UsernameNotFoundException("User not found");

        ResponseUser userResponse = new ModelMapper().map(userEntity, ResponseUser.class);

        List<ResponseOrder> orders= new ArrayList<>();
        userResponse.setOrders(orders);

        return userResponse;
    }
}
