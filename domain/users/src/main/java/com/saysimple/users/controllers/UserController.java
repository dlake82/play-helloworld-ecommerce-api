package com.saysimple.users.controllers;

import com.saysimple.users.dto.UserDto;
import com.saysimple.users.jpa.UserEntity;
import com.saysimple.users.services.UserService;
import com.saysimple.users.vo.RequestUser;
import com.saysimple.users.vo.ResponseUser;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Environment env;
    private final UserService userService;

    @Autowired
    public UserController(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
    }

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's Working in User Serice on port %s", env.getProperty("local.server.port"));
    }

    @PostMapping
    public ResponseEntity<ResponseUser> create(@RequestBody RequestUser user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        userService.create(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping
    public ResponseEntity<List<ResponseUser>> list(){
        Iterable<UserEntity> users = userService.getUserByAll();

        ModelMapper mapper = new ModelMapper();
        List<ResponseUser> result = new ArrayList<>();
        users.forEach(v -> {
            result.add(mapper.map(v, ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUser> get(@PathVariable("userId") String userId){
        ResponseUser user = userService.getUserByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
