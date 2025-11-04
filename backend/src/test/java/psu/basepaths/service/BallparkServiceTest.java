package psu.basepaths.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import psu.basepaths.model.Ballpark;
import psu.basepaths.model.dto.BallparkDTO;
import psu.basepaths.repository.BallparkRepository;

@ExtendWith(MockitoExtension.class)
public class BallparkServiceTest {
    @Mock
    private BallparkRepository ballparkRepository;

    @InjectMocks
    private BallparkServiceImpl ballparkService;

    private static List<BallparkDTO> TEST_VALID_LIST;
    private static List<BallparkDTO> TEST_INVALID_LIST;

    private static int VALID_ADDED = 2;
    private static int VALID_ADDED_DUPLICATE = VALID_ADDED - 1;
    private static Ballpark VALID_BALLPARK_1 = new Ballpark();
    private static Ballpark VALID_BALLPARK_2 = new Ballpark();

    @Mock
    Optional<Ballpark> optionalMock;

    @BeforeAll
     public static void setUp() {
        TEST_VALID_LIST = new ArrayList<BallparkDTO>();
        TEST_INVALID_LIST = new ArrayList<BallparkDTO>();
        
        BallparkDTO validBallpark1 = new BallparkDTO(1, "testName1", "testTeamName1", "Los Angeles", "CA", "USA", 34.0549, 118.2426);
        
        BallparkDTO validBallpark2 = new BallparkDTO(2, "testName2", "testTeamName2", "New York", "NY", "USA", 40.7128, 74.0060);

        BallparkDTO invalidBallpark = null;

        TEST_VALID_LIST.add(validBallpark1);
        TEST_VALID_LIST.add(validBallpark2);

        TEST_INVALID_LIST.add(validBallpark1);
        TEST_INVALID_LIST.add(invalidBallpark);
        TEST_INVALID_LIST.add(validBallpark2);

        VALID_BALLPARK_1.setId(1);
        VALID_BALLPARK_1.setName("testName1");
        VALID_BALLPARK_1.setTeamName("testTeamName1");
        VALID_BALLPARK_1.setCity("Los Angeles");
        VALID_BALLPARK_1.setState("CA");
        VALID_BALLPARK_1.setCountry("USA");
        VALID_BALLPARK_1.setLatitude(34.0549);
        VALID_BALLPARK_1.setLongitude(118.2426);
        
        VALID_BALLPARK_2.setId(2);
        VALID_BALLPARK_2.setName("testName2");
        VALID_BALLPARK_2.setTeamName("testTeamName2");
        VALID_BALLPARK_2.setCity("New York");
        VALID_BALLPARK_2.setState("CNYA");
        VALID_BALLPARK_2.setCountry("USA");
        VALID_BALLPARK_2.setLatitude(40.7128);
        VALID_BALLPARK_2.setLongitude(74.0060);
    }

    // BUT12 – Successful Ballpark Data Load
    @Test
    public void loadData_validInput() {
        when(ballparkRepository.findById(eq(1))).thenReturn(Optional.empty());
        when(ballparkRepository.findById(eq(2))).thenReturn(Optional.empty());

        when(ballparkRepository.save(any(Ballpark.class))).thenReturn(new Ballpark());

        int added = ballparkService.loadBallparks(TEST_VALID_LIST);

        assertNotNull(added);
        assertEquals(added, VALID_ADDED);
    }

    // BUT13 – Ballpark Data Load one exists
    @Test
    public void loadData_existingInput() {
        when(ballparkRepository.findById(eq(1))).thenReturn(Optional.empty());
        when(ballparkRepository.findById(eq(2))).thenReturn(Optional.of(VALID_BALLPARK_2));

        when(ballparkRepository.save(any(Ballpark.class))).thenReturn(new Ballpark());

        int added = ballparkService.loadBallparks(TEST_VALID_LIST);

        assertNotNull(added);
        assertEquals(added, VALID_ADDED_DUPLICATE);
    }

    // BUT14 – Ballpark Invalid Data Load
    @Test
    public void loadData_invalidInput() {
        when(ballparkRepository.findById(eq(1))).thenReturn(Optional.empty());
        
        when(ballparkRepository.save(any(Ballpark.class))).thenReturn(new Ballpark());

        assertThrows(Exception.class, () -> {
            ballparkService.loadBallparks(TEST_INVALID_LIST);
        });
    }
}
