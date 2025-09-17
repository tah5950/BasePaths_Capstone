package psu.basepaths.controller;

import psu.basepaths.model.UserDTO;
import psu.basepaths.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        UserDTO responseUser = userService.registerUser(userDTO);
        return ResponseEntity.ok(responseUser);
    }
}
