package psu.basepaths.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import psu.basepaths.model.Ballpark;
import psu.basepaths.model.BallparkDTO;
import psu.basepaths.model.Game;
import psu.basepaths.model.GameDTO;
import psu.basepaths.repository.GameRepository;
import psu.basepaths.repository.BallparkRepository;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @Mock
    private GameRepository gameRepository;

    @Mock
    private BallparkRepository ballparkRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    private static List<GameDTO> TEST_VALID_LIST;
    private static List<GameDTO> TEST_INVALID_LIST;

    private static int VALID_ADDED = 2;
    private static int VALID_ADDED_DUPLICATE = VALID_ADDED - 1;
    private static Game VALID_GAME_1 = new Game();
    private static Game VALID_GAME_2 = new Game();
    private static Ballpark BALLPARK_1 = new Ballpark();
    private static Ballpark BALLPARK_2 = new Ballpark();
    private static String GAME_1_ID = "27ee4a54-38d9-4893-9c5b-4e481b1d7d57";
    private static String GAME_2_ID = "600e629e-b90f-45c0-827f-1a25e2ed6f5f";

    private String MISSING_BALLPARK_ERROR_STRING = "Ballpark not found for ID: 2";

    @Mock
    Optional<Ballpark> optionalMock;

    @BeforeAll
     public static void setUp() {
        TEST_VALID_LIST = new ArrayList<GameDTO>();
        TEST_INVALID_LIST = new ArrayList<GameDTO>();
        
        GameDTO validGame1 = new GameDTO(GAME_1_ID, new Date(), "testTeamName1", "testTeamName2", 1);
        
        GameDTO validGame2 = new GameDTO(GAME_2_ID, new Date(), "testTeamName3", "testTeamName4", 2);

        GameDTO invalidGame = null;

        TEST_VALID_LIST.add(validGame1);
        TEST_VALID_LIST.add(validGame2);

        TEST_INVALID_LIST.add(validGame1);
        TEST_INVALID_LIST.add(invalidGame);
        TEST_INVALID_LIST.add(validGame2);

        BALLPARK_1.setId(1);
        BALLPARK_1.setName("testName1");
        BALLPARK_1.setTeamName("testTeamName1");
        BALLPARK_1.setCity("Los Angeles");
        BALLPARK_1.setState("CA");
        BALLPARK_1.setCountry("USA");
        BALLPARK_1.setLatitude(34.0549);
        BALLPARK_1.setLongitude(118.2426);
        
        BALLPARK_2.setId(2);
        BALLPARK_2.setName("testName2");
        BALLPARK_2.setTeamName("testTeamName2");
        BALLPARK_2.setCity("New York");
        BALLPARK_2.setState("CNYA");
        BALLPARK_2.setCountry("USA");
        BALLPARK_2.setLatitude(40.7128);
        BALLPARK_2.setLongitude(74.0060);

        VALID_GAME_1.setId(validGame1.gameId());
        VALID_GAME_1.setDate(validGame1.date());
        VALID_GAME_1.setHomeTeam(validGame1.homeTeam());
        VALID_GAME_1.setAwayTeam(validGame1.awayTeam());
        VALID_GAME_1.setBallpark(BALLPARK_1);
        
        VALID_GAME_2.setId(validGame2.gameId());
        VALID_GAME_2.setDate(validGame2.date());
        VALID_GAME_2.setHomeTeam(validGame2.homeTeam());
        VALID_GAME_2.setAwayTeam(validGame2.awayTeam());
        VALID_GAME_2.setBallpark(BALLPARK_2);
    }

    // BUT15 – Successful Game Data Load
    @Test
    public void loadData_validInput() {
        when(gameRepository.findById(eq(GAME_1_ID))).thenReturn(Optional.empty());
        when(gameRepository.findById(eq(GAME_2_ID))).thenReturn(Optional.empty());

        when(ballparkRepository.findById(eq(1))).thenReturn(Optional.of(BALLPARK_1));
        when(ballparkRepository.findById(eq(2))).thenReturn(Optional.of(BALLPARK_2));

        when(gameRepository.save(any(Game.class))).thenReturn(new Game());

        int added = gameService.loadGames(TEST_VALID_LIST);

        assertNotNull(added);
        assertEquals(added, VALID_ADDED);
    }

    // BUT16 – Game Data Load one exists
    @Test
    public void loadData_existingInput() {
        when(gameRepository.findById(eq(GAME_1_ID))).thenReturn(Optional.empty());
        when(gameRepository.findById(eq(GAME_2_ID))).thenReturn(Optional.of(VALID_GAME_2));

        when(ballparkRepository.findById(eq(1))).thenReturn(Optional.of(BALLPARK_1));

        when(gameRepository.save(any(Game.class))).thenReturn(new Game());

        int added = gameService.loadGames(TEST_VALID_LIST);

        assertNotNull(added);
        assertEquals(added, VALID_ADDED_DUPLICATE);
    }

    // BUT17 – Game Invalid Data Load
    @Test
    public void loadData_invalidInput() {
        when(gameRepository.findById(eq(GAME_1_ID))).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            gameService.loadGames(TEST_INVALID_LIST);
        });
    }

    // BUT18 – Game Load Missing Ballpark
    @Test
    public void loadData_missingBallpark() {
        when(gameRepository.findById(eq(GAME_1_ID))).thenReturn(Optional.empty());
        when(gameRepository.findById(eq(GAME_2_ID))).thenReturn(Optional.empty());

        when(ballparkRepository.findById(eq(1))).thenReturn(Optional.of(BALLPARK_1));
        when(ballparkRepository.findById(eq(2))).thenReturn(Optional.empty());

        when(gameRepository.save(any(Game.class))).thenReturn(new Game());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            gameService.loadGames(TEST_VALID_LIST);
        });

        assertTrue(thrown.getMessage().contains(MISSING_BALLPARK_ERROR_STRING));
    }
}
