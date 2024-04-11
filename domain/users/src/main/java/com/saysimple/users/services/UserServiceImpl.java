package com.saysimple.users.services;

import com.saysimple.users.clients.OrderClient;
import com.saysimple.users.dto.UserDto;
import com.saysimple.users.jpa.UserEntity;
import com.saysimple.users.jpa.UserRepository;
import com.saysimple.users.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;
    Environment env;
    RestTemplate restTemplate;
    OrderClient orderClient;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                           OrderClient orderClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.orderClient = orderClient;
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
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new UsernameNotFoundException("User not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        List<ResponseOrder> orders = orderClient.get(userId);
        userDto.setOrders(orders);

        return userDto;
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        ModelMapper mapper = new ModelMapper();
        UserEntity user = userRepository.findByEmail(email);
        return mapper.map(user, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(), true, true,
                true, true, new ArrayList<>());
    }
}
