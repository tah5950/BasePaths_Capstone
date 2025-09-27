package psu.basepaths.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import psu.basepaths.model.User;
import psu.basepaths.model.UserDTO;
import psu.basepaths.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String VALID_USERNAME = "validUser";
    private static final String VALID_PASSWORD = "validPass1!";
    private static final String VALID_PASSWORD_HASHED = passwordEncoder.encode(VALID_PASSWORD);

    private static final String INVALID_USERNAME = "invalid_User";
    private static final String INVALID_PASSWORD = "invalidPass";

    private static final String EMPTY_STRING = "";

    private static final Long ID = (long) 1;

    private static final String DUPLICATE_USERNAME_ERROR_STRING = "Username Already Exists";
    private static final String USERNAME_ERROR_STRING = "Username must be 6- 16 alphanumeric characters only.";
    private static final String PASSWORD_ERROR_STRING = "Password must be 8-32 characters with at least one lowercase letter, one uppercase letter, one number, and one symbol";

    private static User returnValidUser;

    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    Optional<User> optionalMock;

    @BeforeAll
    public static void setUp() {
        returnValidUser = new User();
        returnValidUser.setId(ID);
        returnValidUser.setUsername(VALID_USERNAME);
        returnValidUser.setPasswordHash(VALID_PASSWORD_HASHED);
    }

    // BUT1 - Register Valid User
    @Test
    public void registerUser_validInput() {
        UserDTO user = new UserDTO(null, VALID_USERNAME, VALID_PASSWORD);

        when(userRepository.save(any(User.class))).thenReturn(returnValidUser);

        UserDTO registeredUser = userService.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals(user.username(), registeredUser.username());
        assertEquals(registeredUser.password(), VALID_PASSWORD_HASHED);
    }


    // BUT2 – Register User fails with empty username
    @Test
    public void registerUser_emptyUsername() {
        UserDTO user = new UserDTO(null, EMPTY_STRING, VALID_PASSWORD);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(user);
        });

        assertTrue(thrown.getMessage().contains(USERNAME_ERROR_STRING));
    }

    // BUT3 – Register User fails with empty password
    @Test
    public void registerUser_emptyPassword() {
        UserDTO user = new UserDTO(null, VALID_USERNAME, EMPTY_STRING);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(user);
        });

        assertTrue(thrown.getMessage().contains(PASSWORD_ERROR_STRING));
    }


    // BUT4 – Register Invalid Username
    @Test
    public void registerUser_invalidUsername() {
        UserDTO user = new UserDTO(null, INVALID_USERNAME, VALID_PASSWORD);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(user);
        });

        assertTrue(thrown.getMessage().contains(USERNAME_ERROR_STRING));
    }

    // BUT5 – Register Invalid Password
    @Test
    public void registerUser_invalidPassword() {
        UserDTO user = new UserDTO(null, VALID_USERNAME, INVALID_PASSWORD);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(user);
        });

        assertTrue(thrown.getMessage().contains(PASSWORD_ERROR_STRING));
    }

    // BUT6 – Register Duplicate Username
    @Test
    public void registerUser_duplicateUsername() {
        UserDTO user = new UserDTO(null, VALID_USERNAME, VALID_PASSWORD);

        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(user);
        });

        assertTrue(thrown.getMessage().contains(DUPLICATE_USERNAME_ERROR_STRING));
    }
}
