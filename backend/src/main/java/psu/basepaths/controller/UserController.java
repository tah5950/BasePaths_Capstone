package psu.basepaths.controller;

import psu.basepaths.model.UserDTO;
import psu.basepaths.service.JwtService;
import psu.basepaths.service.UserService;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService){
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try{
            UserDTO responseUser = userService.registerUser(userDTO);
            String token = jwtService.generateToken(responseUser.username());
            return ResponseEntity.ok(Map.of("token", token));
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO){
        return userService.authenticate(userDTO).map(user -> {
            String token = jwtService.generateToken(user.username());
            return ResponseEntity.ok(Map.of("token", token));
        }).orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid Username or Password")));
    }
}
