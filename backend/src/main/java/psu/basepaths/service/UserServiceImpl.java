package psu.basepaths.service;

import psu.basepaths.model.User;
import psu.basepaths.model.UserDTO;
import psu.basepaths.repository.UserRepository;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final String usernameRegex = "^[0-9A-Za-z]{6,16}$";
    private final String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$";


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) throws IllegalArgumentException{
        validateUsername(userDTO.username());
        validatePassword(userDTO.password());

        if(userRepository.existsUserByUsername(userDTO.username())){
            throw new IllegalArgumentException("Username Already Exists");
        }

        User user = convertToEntity(userDTO);
        User registeredUser = new User();

        registeredUser = userRepository.save(user);

        return convertToDTO(registeredUser);
    }

    @Override
    public Optional<UserDTO> authenticate(UserDTO userDTO) {
        return userRepository.findByUsername(userDTO.username())
                .filter(user -> passwordEncoder.matches(userDTO.password(), user.getPasswordHash()))
                .map(this :: convertToDTO);
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

    private void validateUsername(String username){
        boolean match = username.matches(usernameRegex);
        if(!match){
            throw new IllegalArgumentException("Username must be 6- 16 alphanumeric characters only.");
        }
    }

    private void validatePassword(String password){
        boolean match = password.matches(passwordRegex);
        if(!match){
            throw new IllegalArgumentException("Password must be 8-32 characters with at least one lowercase letter, one uppercase letter, one number, and one symbol");
        }
    }
}
