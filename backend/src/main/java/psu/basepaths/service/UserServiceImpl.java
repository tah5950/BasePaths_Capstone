package psu.basepaths.service;

import psu.basepaths.model.User;
import psu.basepaths.model.UserDTO;
import psu.basepaths.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO){
        User user = convertToEntity(userDTO);
        User registeredUser = userRepository.save(user);
        return convertToDTO(registeredUser);
    }

    // Convert User Entity to UserDTO
    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getPasswordHash());
    }

    // Convert UserDTO to User Entity
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.username());
        user.setPasswordHash(passwordEncoder.encode(userDTO.password()));
        return user;
    }
}
