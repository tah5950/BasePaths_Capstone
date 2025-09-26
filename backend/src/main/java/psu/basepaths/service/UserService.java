package psu.basepaths.service;

import java.util.Optional;

import psu.basepaths.model.UserDTO;

public interface UserService {
    public UserDTO registerUser(UserDTO userDTO);
    public Optional<UserDTO> authenticate(UserDTO userDTO);
}
