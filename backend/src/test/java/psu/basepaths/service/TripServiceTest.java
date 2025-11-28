package psu.basepaths.service;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.time.ZoneId;
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

import psu.basepaths.model.Trip;
import psu.basepaths.model.TripStop;
import psu.basepaths.model.dto.BallparkDTO;
import psu.basepaths.model.dto.GameDTO;
import psu.basepaths.model.dto.TripDTO;
import psu.basepaths.model.dto.TripStopDTO;
import psu.basepaths.repository.TripRepository;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {
    @Mock
    private GameService gameService;

    @Mock
    private BallparkService ballparkService;

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private TripServiceImpl tripService;

    private static final double START_LAT_VALID = 32.72;
    private static final double START_LON_VALID = -117.16;
    private static final double END_LAT_VALID = 38.58;
    private static final double END_LON_VALID = -121.49;
    private static final double LAT_INVALID = 100.72;
    private static final double LON_INVALID = 200.72;

    private static int MAX_HOURS_PER_DAY = 8;

    private static Date TEST_DATE_START;
    private static Date TEST_DATE_START_UPDATE;
    private static Date TEST_DATE_END_VALID;
    private static Date TEST_DATE_END_INVALID;

    private static Trip VALID_TRIP_1;
    private static Trip VALID_TRIP_1_TO_UPDATE;
    private static Trip VALID_TRIP_2_NO_STOPS;
    private static Trip VALID_TRIP_GEN_INPUT;
    private static Trip VALID_TRIP_GENERATED;
    private static Trip VALID_TRIP_1_UPDATED;
    private static Trip VALID_TRIP_1_UPDATED_DATES;

    private static TripStop VALID_TRIP_STOP_1;
    private static TripStop VALID_TRIP_STOP_2;

    private static TripDTO VALID_TRIP_DTO_1;
    private static TripDTO VALID_TRIP_DTO_GEN_INPUT;
    private static TripDTO VALID_TRIP_DTO_GENERATED;
    private static TripDTO VALID_TRIP_DTO_1_UPDATED;
    private static TripDTO VALID_TRIP_DTO_1_UPDATED_DATES;

    private static TripStopDTO VALID_TRIP_STOP_DTO_1;
    private static TripStopDTO VALID_TRIP_STOP_DTO_2;

    private static TripStopDTO VALID_TRIP_STOP_DTO_START;
    private static TripStopDTO VALID_TRIP_STOP_DTO_SD;
    private static TripStopDTO VALID_TRIP_STOP_DTO_LA;
    private static TripStopDTO VALID_TRIP_STOP_DTO_SF;
    private static TripStopDTO VALID_TRIP_STOP_DTO_END;

    private static List<GameDTO> GAMES;
    private static List<BallparkDTO> BALLPARKS;

    @BeforeAll
    public static void setUp() {
        LocalDate localDateStart = LocalDate.of(2026, 3, 25);
        TEST_DATE_START = Date.from(localDateStart.atStartOfDay(ZoneId.of("UTC")).toInstant());

        LocalDate localDateStartUpdate = LocalDate.of(2026, 3, 24);
        TEST_DATE_START_UPDATE = Date.from(localDateStartUpdate.atStartOfDay(ZoneId.of("UTC")).toInstant());

        LocalDate localDateEndValid = LocalDate.of(2026, 3, 29);
        TEST_DATE_END_VALID = Date.from(localDateEndValid.atStartOfDay(ZoneId.of("UTC")).toInstant());

        LocalDate localDateEndInvalid = LocalDate.of(2026, 10, 30);
        TEST_DATE_END_INVALID = Date.from(localDateEndInvalid.atStartOfDay(ZoneId.of("UTC")).toInstant());

        VALID_TRIP_STOP_DTO_1 = new TripStopDTO(
            null,
            Date.from(LocalDate.of(2026, 3, 26).atStartOfDay(ZoneId.of("UTC")).toInstant()),
            "San Diego",
            1,
            "game1"
        );

        VALID_TRIP_STOP_DTO_2 = new TripStopDTO(
            null,
            Date.from(LocalDate.of(2026, 3, 27).atStartOfDay(ZoneId.of("UTC")).toInstant()),
            "Los Angeles",
            2,
            "game2"
        );

        VALID_TRIP_DTO_1 = new TripDTO(
            null, 
            "Test Trip 1", 
            TEST_DATE_START, 
            TEST_DATE_END_VALID, 
            START_LAT_VALID, 
            START_LON_VALID, 
            END_LAT_VALID, 
            END_LON_VALID, 
            false, 
            MAX_HOURS_PER_DAY, 
            1L, 
            List.of(VALID_TRIP_STOP_DTO_1, VALID_TRIP_STOP_DTO_2)
        );

        VALID_TRIP_DTO_1_UPDATED = new TripDTO(
            1L, 
            "Test Trip 1 Updated", 
            TEST_DATE_START, 
            TEST_DATE_END_VALID, 
            START_LAT_VALID, 
            START_LON_VALID, 
            END_LAT_VALID, 
            END_LON_VALID, 
            false, 
            MAX_HOURS_PER_DAY, 
            1L, 
            List.of(VALID_TRIP_STOP_DTO_1, VALID_TRIP_STOP_DTO_2)
        );

        VALID_TRIP_DTO_1_UPDATED_DATES = new TripDTO(
            1L, 
            "Test Trip 1 Updated", 
            TEST_DATE_START_UPDATE,
            TEST_DATE_END_VALID,
            START_LAT_VALID, 
            START_LON_VALID, 
            END_LAT_VALID, 
            END_LON_VALID, 
            true, 
            MAX_HOURS_PER_DAY, 
            1L, 
            List.of(VALID_TRIP_STOP_DTO_1, VALID_TRIP_STOP_DTO_2)
        );

        VALID_TRIP_STOP_1 = new TripStop();
        VALID_TRIP_STOP_1.setId(1L);
        VALID_TRIP_STOP_1.setDate(Date.from(LocalDate.of(2026, 3, 26).atStartOfDay(ZoneId.of("UTC")).toInstant()));
        VALID_TRIP_STOP_1.setLocation("San Diego");
        VALID_TRIP_STOP_1.setBallparkId(1);
        VALID_TRIP_STOP_1.setGameId("game1");

        VALID_TRIP_STOP_2 = new TripStop();
        VALID_TRIP_STOP_2.setId(2L);
        VALID_TRIP_STOP_2.setDate(Date.from(LocalDate.of(2026, 3, 27).atStartOfDay(ZoneId.of("UTC")).toInstant()));
        VALID_TRIP_STOP_2.setLocation("Los Angeles");
        VALID_TRIP_STOP_2.setBallparkId(2);
        VALID_TRIP_STOP_2.setGameId("game2");

        VALID_TRIP_1 = new Trip();
        VALID_TRIP_1.setId(1L);
        VALID_TRIP_1.setName("Test Trip 1");
        VALID_TRIP_1.setStartDate(TEST_DATE_START);
        VALID_TRIP_1.setEndDate(TEST_DATE_END_VALID);
        VALID_TRIP_1.setStartLatitude(START_LAT_VALID);
        VALID_TRIP_1.setStartLongitude(START_LON_VALID);
        VALID_TRIP_1.setEndLatitude(END_LAT_VALID);
        VALID_TRIP_1.setEndLongitude(END_LON_VALID);
        VALID_TRIP_1.setMaxHoursPerDay(MAX_HOURS_PER_DAY);
        VALID_TRIP_1.setUserId(1L);
        VALID_TRIP_1.setTripStops(new ArrayList<>(List.of(VALID_TRIP_STOP_1, VALID_TRIP_STOP_2)));

        VALID_TRIP_1_TO_UPDATE = new Trip();
        VALID_TRIP_1_TO_UPDATE.setId(1L);
        VALID_TRIP_1_TO_UPDATE.setName("Test Trip 1");
        VALID_TRIP_1_TO_UPDATE.setStartDate(TEST_DATE_START);
        VALID_TRIP_1_TO_UPDATE.setEndDate(TEST_DATE_END_VALID);
        VALID_TRIP_1_TO_UPDATE.setStartLatitude(START_LAT_VALID);
        VALID_TRIP_1_TO_UPDATE.setStartLongitude(START_LON_VALID);
        VALID_TRIP_1_TO_UPDATE.setEndLatitude(END_LAT_VALID);
        VALID_TRIP_1_TO_UPDATE.setEndLongitude(END_LON_VALID);
        VALID_TRIP_1_TO_UPDATE.setMaxHoursPerDay(MAX_HOURS_PER_DAY);
        VALID_TRIP_1_TO_UPDATE.setUserId(1L);
        VALID_TRIP_1_TO_UPDATE.setTripStops(new ArrayList<>(List.of(VALID_TRIP_STOP_1, VALID_TRIP_STOP_2)));

        VALID_TRIP_1_UPDATED = new Trip();
        VALID_TRIP_1_UPDATED.setId(1L);
        VALID_TRIP_1_UPDATED.setName("Test Trip 1 Updated");
        VALID_TRIP_1_UPDATED.setStartDate(TEST_DATE_START);
        VALID_TRIP_1_UPDATED.setEndDate(TEST_DATE_END_VALID);
        VALID_TRIP_1_UPDATED.setStartLatitude(START_LAT_VALID);
        VALID_TRIP_1_UPDATED.setStartLongitude(START_LON_VALID);
        VALID_TRIP_1_UPDATED.setEndLatitude(END_LAT_VALID);
        VALID_TRIP_1_UPDATED.setEndLongitude(END_LON_VALID);
        VALID_TRIP_1_UPDATED.setMaxHoursPerDay(MAX_HOURS_PER_DAY);
        VALID_TRIP_1_UPDATED.setUserId(1L);
        VALID_TRIP_1_UPDATED.setTripStops(new ArrayList<>(List.of(VALID_TRIP_STOP_1, VALID_TRIP_STOP_2)));

        VALID_TRIP_1_UPDATED_DATES = new Trip();
        VALID_TRIP_1_UPDATED_DATES.setId(1L);
        VALID_TRIP_1_UPDATED_DATES.setName("Test Trip 1 Updated");
        VALID_TRIP_1_UPDATED_DATES.setStartDate(TEST_DATE_START_UPDATE);
        VALID_TRIP_1_UPDATED_DATES.setEndDate(TEST_DATE_END_VALID);
        VALID_TRIP_1_UPDATED_DATES.setStartLatitude(null);
        VALID_TRIP_1_UPDATED_DATES.setStartLongitude(null);
        VALID_TRIP_1_UPDATED_DATES.setEndLatitude(null);
        VALID_TRIP_1_UPDATED_DATES.setEndLongitude(null);
        VALID_TRIP_1_UPDATED_DATES.setMaxHoursPerDay(null);
        VALID_TRIP_1_UPDATED_DATES.setUserId(1L);
        VALID_TRIP_1_UPDATED_DATES.setTripStops(new ArrayList<>());

        VALID_TRIP_2_NO_STOPS = new Trip();
        VALID_TRIP_2_NO_STOPS.setId(1L);
        VALID_TRIP_2_NO_STOPS.setName("Test Trip 1");
        VALID_TRIP_2_NO_STOPS.setStartDate(TEST_DATE_START);
        VALID_TRIP_2_NO_STOPS.setEndDate(TEST_DATE_END_VALID);
        VALID_TRIP_2_NO_STOPS.setStartLatitude(START_LAT_VALID);
        VALID_TRIP_2_NO_STOPS.setStartLongitude(START_LON_VALID);
        VALID_TRIP_2_NO_STOPS.setEndLatitude(END_LAT_VALID);
        VALID_TRIP_2_NO_STOPS.setEndLongitude(END_LON_VALID);
        VALID_TRIP_2_NO_STOPS.setMaxHoursPerDay(MAX_HOURS_PER_DAY);
        VALID_TRIP_2_NO_STOPS.setUserId(1L);

        initializeGeneratedTestValues();
    }

    // BUT19 - Create Trip Valid Input
    @Test
    public void createTrip_validInput() {
        when(tripRepository.save(any(Trip.class))).thenReturn(VALID_TRIP_1);

        TripDTO result = tripService.createTrip(VALID_TRIP_DTO_1);

        assertNotNull(result);
        assertEquals(result.name(), "Test Trip 1");
        assertEquals(result.startDate(), TEST_DATE_START);
        assertEquals(result.endDate(), TEST_DATE_END_VALID);
        assertNotNull(result.tripStops());
        assertEquals(2, result.tripStops().size());
        assertEquals("San Diego", result.tripStops().get(0).location());
        assertEquals("game2", result.tripStops().get(1).gameId());
    }

    // BUT20 - Create Trip Blank Input
    @Test
    public void createTrip_blankInput() {
        TripDTO invalid = new TripDTO(
            null, 
            "", 
            TEST_DATE_START, 
            TEST_DATE_END_VALID, 
            START_LAT_VALID, 
            START_LON_VALID, 
            END_LAT_VALID, 
            END_LON_VALID, 
            false, 
            MAX_HOURS_PER_DAY, 
            1L, 
            List.of(VALID_TRIP_STOP_DTO_1, VALID_TRIP_STOP_DTO_2)
        );

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tripService.createTrip(invalid);
        });

        assertTrue(thrown.getMessage().contains("Name must not be blank"));
    }

    // BUT21 - Create Trip Blank Start Date
    @Test
    public void createTrip_blankStartDate() {
        TripDTO invalid = new TripDTO(
            null, 
            "Test Trip 1", 
            null, 
            TEST_DATE_END_VALID, 
            START_LAT_VALID, 
            START_LON_VALID, 
            END_LAT_VALID, 
            END_LON_VALID, 
            false, 
            MAX_HOURS_PER_DAY, 
            1L, 
            List.of(VALID_TRIP_STOP_DTO_1, VALID_TRIP_STOP_DTO_2)
        );

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tripService.createTrip(invalid);
        });

        assertTrue(thrown.getMessage().contains("Start Date must not be empty"));
    }

    // BUT22 - Create Trip Blank End Date
    @Test
    public void createTrip_blankEndDate() {
        TripDTO invalid = new TripDTO(
            null, 
            "Test Trip 1", 
            TEST_DATE_START, 
            null, 
            START_LAT_VALID, 
            START_LON_VALID, 
            END_LAT_VALID, 
            END_LON_VALID, 
            false, 
            MAX_HOURS_PER_DAY, 
            1L, 
            List.of(VALID_TRIP_STOP_DTO_1, VALID_TRIP_STOP_DTO_2)
        );

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tripService.createTrip(invalid);
        });

        assertTrue(thrown.getMessage().contains("End Date must not be empty"));
    }

    // BUT23 - Create Trip Start Date after End Date
    @Test
    public void createTrip_StartAfterEnd() {
        TripDTO invalid = new TripDTO(
            null, 
            "Test Trip 1", 
            TEST_DATE_END_VALID,
            TEST_DATE_START, 
            START_LAT_VALID, 
            START_LON_VALID, 
            END_LAT_VALID, 
            END_LON_VALID, 
            false, 
            MAX_HOURS_PER_DAY, 
            1L, 
            List.of(VALID_TRIP_STOP_DTO_1, VALID_TRIP_STOP_DTO_2)
        );

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tripService.createTrip(invalid);
        });

        assertTrue(thrown.getMessage().contains("Start Date must be before End Date"));
    }

    // BUT37 - Create Trip Longer than 2 Weeks
    @Test
    public void createTrip_tooLong() {
        TripDTO invalid = new TripDTO(
            null, 
            "Test Trip 1", 
            TEST_DATE_START, 
            TEST_DATE_END_INVALID, 
            START_LAT_VALID, 
            START_LON_VALID, 
            END_LAT_VALID, 
            END_LON_VALID, 
            false, 
            MAX_HOURS_PER_DAY, 
            1L, 
            List.of(VALID_TRIP_STOP_DTO_1, VALID_TRIP_STOP_DTO_2)
        );

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tripService.createTrip(invalid);
        });

        assertTrue(thrown.getMessage().contains("Only trips of 2 weeks or less are supported currently"));
    }

    // BUT24 - Create Trip no Trip Stops
    @Test
    public void createTrip_noStops() {
        TripDTO noStops = new TripDTO(
            null, 
            "Test Trip 1", 
            TEST_DATE_START, 
            TEST_DATE_END_VALID, 
            START_LAT_VALID, 
            START_LON_VALID, 
            END_LAT_VALID, 
            END_LON_VALID, 
            false, 
            MAX_HOURS_PER_DAY, 
            1L, 
            null
        );
        
        when(tripRepository.save(any(Trip.class))).thenReturn(VALID_TRIP_2_NO_STOPS);

        TripDTO result = tripService.createTrip(noStops);

        assertNotNull(result);
        assertEquals(result.name(), "Test Trip 1");
        assertEquals(result.startDate(), TEST_DATE_START);
        assertEquals(result.endDate(), TEST_DATE_END_VALID);
        assertNotNull(result.tripStops());
        assertEquals(0, result.tripStops().size());
    }

    // BUT25 – Get User trips returns valid DTO Trips
    @Test
    public void getUserTrips_validTrips(){
        List<Trip> validList = List.of(VALID_TRIP_1, VALID_TRIP_2_NO_STOPS);

        when(tripRepository.findByUserId(1L)).thenReturn(Optional.of(validList));

        List<TripDTO> result = tripService.getUserTrips(1L);

        assertNotNull(result);

        assertEquals(result.get(0).name(), "Test Trip 1");
        assertEquals(result.get(0).startDate(), TEST_DATE_START);
        assertEquals(result.get(0).endDate(), TEST_DATE_END_VALID);
        assertNotNull(result.get(0).tripStops());
        assertEquals(2, result.get(0).tripStops().size());
        assertEquals("San Diego", result.get(0).tripStops().get(0).location());
        assertEquals("game2", result.get(0).tripStops().get(1).gameId());

        assertEquals(result.get(1).name(), "Test Trip 1");
        assertEquals(result.get(1).startDate(), TEST_DATE_START);
        assertEquals(result.get(1).endDate(), TEST_DATE_END_VALID);
        assertNotNull(result.get(1).tripStops());
        assertEquals(0, result.get(1).tripStops().size());
    }

    // BUT26 – Get User trips when no trips are saved
    @Test
    public void getUserTrips_NoSavedTrips(){
        when(tripRepository.findByUserId(1L)).thenReturn(Optional.empty());

        List<TripDTO> result = tripService.getUserTrips(1L);

        assertNotNull(result);
        assertEquals(result.size(), 0);
    }

    // BUT27 – Get trip by id returns valid DTO Trip
    @Test
    public void getTripById_validTrip(){
        when(tripRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(VALID_TRIP_1));

        TripDTO result = tripService.getTripById(1L, 1L);

        assertNotNull(result);
        assertEquals(result.name(), "Test Trip 1");
        assertEquals(result.startDate(), TEST_DATE_START);
        assertEquals(result.endDate(), TEST_DATE_END_VALID);
        assertNotNull(result.tripStops());
        assertEquals(2, result.tripStops().size());
        assertEquals("San Diego", result.tripStops().get(0).location());
        assertEquals("game2", result.tripStops().get(1).gameId());
        assertEquals(result.tripId(), 1L);
        assertEquals(result.userId(), 1L);
    }

    // BUT28 – Get trip by id throws exception when repository comes back empty
    @Test
    public void getTripById_tripNotFound(){
        when(tripRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            tripService.getTripById(1L, 1L);
        });

        assertTrue(thrown.getMessage().contains("Trip not found or not accessible by user: 1"));
    }

    // BUT29 - Generate Trip with valid Trip
    @Test
    public void generateTrip_validTrip(){
        when(ballparkService.getAllBallparks()).thenReturn(BALLPARKS);
        when(gameService.getGameByDateRange(TEST_DATE_START, TEST_DATE_END_VALID)).thenReturn(GAMES);
        when(tripRepository.findById(VALID_TRIP_DTO_GENERATED.tripId())).thenReturn(Optional.of(VALID_TRIP_GEN_INPUT));
        when(tripRepository.save(any(Trip.class))).thenReturn(VALID_TRIP_GEN_INPUT);

        TripDTO result = tripService.generateTrip(VALID_TRIP_DTO_GEN_INPUT);

        assertNotNull(result);
        assertEquals(result.name(), "Generated Test Trip");
        assertEquals(result.startDate(), TEST_DATE_START);
        assertEquals(result.endDate(), TEST_DATE_END_VALID);
        assertTrue(result.isGenerated());
        assertNotNull(result.tripStops());
        assertEquals(5, result.tripStops().size());
        assertEquals("Start", result.tripStops().get(0).location());
        assertEquals("San Diego", result.tripStops().get(1).location());
        assertEquals("Los Angeles", result.tripStops().get(2).location());
        assertEquals("San Francisco", result.tripStops().get(3).location());
        assertEquals("End", result.tripStops().get(4).location());
    }

    // BUT30A – Generate Trip with invalid Start Latitude
    @Test
    public void generateTrip_invalidStartLat(){
        TripDTO invalid = new TripDTO(
            null, 
            "Test Generation", 
            TEST_DATE_START, 
            TEST_DATE_END_VALID, 
            LAT_INVALID, 
            START_LON_VALID, 
            END_LAT_VALID, 
            END_LON_VALID, 
            false, 
            MAX_HOURS_PER_DAY, 
            1L, 
            List.of(VALID_TRIP_STOP_DTO_1, VALID_TRIP_STOP_DTO_2)
        );

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tripService.generateTrip(invalid);
        });

        assertTrue(thrown.getMessage().contains("Invalid Start Latitude"));
    }

    // BUT30B – Generate Trip with invalid Start Longitude
    @Test
    public void generateTrip_invalidStartLon(){
        TripDTO invalid = new TripDTO(
            null, 
            "Test Generation", 
            TEST_DATE_START, 
            TEST_DATE_END_VALID, 
            START_LAT_VALID, 
            LON_INVALID, 
            END_LAT_VALID, 
            END_LON_VALID, 
            false, 
            MAX_HOURS_PER_DAY, 
            1L, 
            List.of(VALID_TRIP_STOP_DTO_1, VALID_TRIP_STOP_DTO_2)
        );

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tripService.generateTrip(invalid);
        });

        assertTrue(thrown.getMessage().contains("Invalid Start Longitude"));
    }

    // BUT30C – Generate Trip with invalid End Latitude
    @Test
    public void generateTrip_invalidEndLat(){
        TripDTO invalid = new TripDTO(
            null, 
            "Test Generation", 
            TEST_DATE_START, 
            TEST_DATE_END_VALID, 
            START_LAT_VALID, 
            START_LON_VALID, 
            LAT_INVALID, 
            END_LON_VALID, 
            false, 
            MAX_HOURS_PER_DAY, 
            1L, 
            List.of(VALID_TRIP_STOP_DTO_1, VALID_TRIP_STOP_DTO_2)
        );

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tripService.generateTrip(invalid);
        });

        assertTrue(thrown.getMessage().contains("Invalid End Latitude"));
    }

    // BUT30D – Generate Trip with invalid End Longitude
    @Test
    public void generateTrip_invalidEndLon(){
        TripDTO invalid = new TripDTO(
            null, 
            "Test Generation", 
            TEST_DATE_START, 
            TEST_DATE_END_VALID, 
            START_LAT_VALID, 
            START_LAT_VALID, 
            END_LAT_VALID, 
            LON_INVALID, 
            false, 
            MAX_HOURS_PER_DAY, 
            1L, 
            List.of(VALID_TRIP_STOP_DTO_1, VALID_TRIP_STOP_DTO_2)
        );

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tripService.generateTrip(invalid);
        });

        assertTrue(thrown.getMessage().contains("Invalid End Longitude"));
    }

    // BUT30E – Generate Trip with invalid Max Hours Per Day
    @Test
    public void generateTrip_invalidHoursPerDay(){
        TripDTO invalid = new TripDTO(
            null, 
            "Test Generation", 
            TEST_DATE_START, 
            TEST_DATE_END_VALID, 
            START_LAT_VALID, 
            START_LAT_VALID, 
            END_LAT_VALID, 
            END_LON_VALID, 
            false, 
            null, 
            1L, 
            List.of(VALID_TRIP_STOP_DTO_1, VALID_TRIP_STOP_DTO_2)
        );

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tripService.generateTrip(invalid);
        });

        assertTrue(thrown.getMessage().contains("Max Hours Per Day must not be empty"));
    }

    // BUT31 – Delete Valid Trip
    @Test
    public void deleteTrip_validTrip(){
        when(tripRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(VALID_TRIP_1));

        assertDoesNotThrow(() -> tripService.deleteTrip(1L, 1L));
    }

    // BUT32 – Delete Invalid Trip
    @Test
    public void deleteTrip_invalidTrip(){
        when(tripRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            tripService.deleteTrip(1L, 1L);
        });

        assertTrue(thrown.getMessage().contains("Trip not found or not accessible by user: "));
    }

    // BUT33 - Update Valid Trip
    @Test
    public void updateTrip_validTrip(){
        when(tripRepository.findById(1L)).thenReturn(Optional.of(VALID_TRIP_1_TO_UPDATE));
        when(tripRepository.save(any(Trip.class))).thenReturn(VALID_TRIP_1_UPDATED);

        TripDTO updated = tripService.updateTrip(VALID_TRIP_DTO_1_UPDATED, false);

        assertEquals(VALID_TRIP_DTO_1_UPDATED.name(), updated.name());
        assertEquals(VALID_TRIP_DTO_1_UPDATED.endDate(), updated.endDate());
    }

    // BUT34 - Update Invalid Trip
    @Test
    public void updateTrip_invalidTrip(){
        TripDTO invalid = new TripDTO(
            null, 
            "Test Trip 1", 
            TEST_DATE_END_VALID,
            TEST_DATE_START, 
            START_LAT_VALID, 
            START_LON_VALID, 
            END_LAT_VALID, 
            END_LON_VALID, 
            false, 
            MAX_HOURS_PER_DAY, 
            1L, 
            List.of(VALID_TRIP_STOP_DTO_1, VALID_TRIP_STOP_DTO_2)
        );

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tripService.updateTrip(invalid, false);
        });

        assertTrue(thrown.getMessage().contains("Start Date must be before End Date"));
    }

    // BUT35 - Update Missing Trip
    @Test
    public void updateTrip_missingTrip(){
        when(tripRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            tripService.updateTrip(VALID_TRIP_DTO_1_UPDATED, false);
        });

        assertTrue(thrown.getMessage().contains("Trip not found with id 1"));
    }

    // BUT36 - Update Valid Trip Dates
    @Test
    public void updateTripDates_validTrip(){
        when(tripRepository.findById(1L)).thenReturn(Optional.of(VALID_TRIP_1_TO_UPDATE));
        when(tripRepository.save(any(Trip.class))).thenReturn(VALID_TRIP_1_UPDATED_DATES);

        TripDTO updated = tripService.updateTrip(VALID_TRIP_DTO_1_UPDATED_DATES, false);

        assertEquals(VALID_TRIP_DTO_1_UPDATED_DATES.name(), updated.name());
        assertEquals(VALID_TRIP_DTO_1_UPDATED_DATES.startDate(), updated.startDate());
        assertNull(updated.startLatitude());
        assertNull(updated.startLongitude());
        assertNull(updated.endLatitude());
        assertNull(updated.endLongitude());
        assertNull(updated.maxHoursPerDay());
        assertFalse(updated.isGenerated());
        assertEquals(0, updated.tripStops().size());
    }
    
    private static void initializeGeneratedTestValues(){
        VALID_TRIP_STOP_DTO_START = new TripStopDTO(
            null,
            Date.from(LocalDate.of(2026, 3, 25).atStartOfDay(ZoneId.of("UTC")).toInstant()),
            "Start",
            null,
            null
        );

        VALID_TRIP_STOP_DTO_SD = new TripStopDTO(
            null,
            Date.from(LocalDate.of(2026, 3, 26).atStartOfDay(ZoneId.of("UTC")).toInstant()),
            "San Diego",
            2680,
            "1773b103-88ec-4b64-8163-5fb06b605339"
        );

        VALID_TRIP_STOP_DTO_LA = new TripStopDTO(
            null,
            Date.from(LocalDate.of(2026, 3, 27).atStartOfDay(ZoneId.of("UTC")).toInstant()),
            "Los Angeles",
            22,
            "70ca6bd1-99b7-4986-bf4a-ea0fa6d08d2c"
        );

        VALID_TRIP_STOP_DTO_SF = new TripStopDTO(
            null,
            Date.from(LocalDate.of(2026, 3, 28).atStartOfDay(ZoneId.of("UTC")).toInstant()),
            "San Francisco",
            2395,
            "0d3c3efc-80cb-4323-a44c-3e6d40555080"
        );

        VALID_TRIP_STOP_DTO_END = new TripStopDTO(
            null,
            Date.from(LocalDate.of(2026, 3, 29).atStartOfDay(ZoneId.of("UTC")).toInstant()),
            "End",
            null,
            null
        );

       GameDTO game1 = new GameDTO(
            "1773b103-88ec-4b64-8163-5fb06b605339",
            Date.from(LocalDate.of(2026, 3, 26).atStartOfDay(ZoneId.of("UTC")).toInstant()),
            "San Diego Padres",
            "Detroit Tigers",
            2680
        );

        GameDTO game2 = new GameDTO(
            "70ca6bd1-99b7-4986-bf4a-ea0fa6d08d2c",
            Date.from(LocalDate.of(2026, 3, 27).atStartOfDay(ZoneId.of("UTC")).toInstant()),
            "Los Angeles Dodgers",
            "Arizona Diamondbacks",
            22
        );

        GameDTO game3 = new GameDTO(
            "0d3c3efc-80cb-4323-a44c-3e6d40555080",
            Date.from(LocalDate.of(2026, 3, 28).atStartOfDay(ZoneId.of("UTC")).toInstant()),
            "San Francisco Giants",
            "New York Yankees",
            2395
        );

        GAMES = List.of(game1, game2, game3);

        BallparkDTO ballpark1 = new BallparkDTO(
            2680,
            "PETCO Park",
            "San Diego Padres",
            "San Diego",
            "CA",
            "USA",
            32.70752890000001,
            -117.1568083
        );

        BallparkDTO ballpark2 = new BallparkDTO(
            22,
            "Dodger Stadium",
            "Los Angeles Dodgers",
            "Los Angeles",
            "CA",
            "USA",
            34.0745409,
            -118.2408881
        );

        BallparkDTO ballpark3 = new BallparkDTO(
            2395,
            "Oracle Park",
            "San Francisco Giants",
            "San Francisco",
            "CA",
            "USA",
            37.7784199,
            -122.3906212
        );

        BALLPARKS = List.of(ballpark1, ballpark2, ballpark3);

        VALID_TRIP_DTO_GENERATED = new TripDTO(
            1L, 
            "Generated Test Trip", 
            TEST_DATE_START, 
            TEST_DATE_END_VALID, 
            START_LAT_VALID, 
            START_LON_VALID, 
            END_LAT_VALID, 
            END_LON_VALID, 
            true, 
            MAX_HOURS_PER_DAY, 
            1L, 
            List.of(
                VALID_TRIP_STOP_DTO_START,
                VALID_TRIP_STOP_DTO_SD,
                VALID_TRIP_STOP_DTO_LA,
                VALID_TRIP_STOP_DTO_SF,
                VALID_TRIP_STOP_DTO_END
            )
        );

        VALID_TRIP_DTO_GEN_INPUT = new TripDTO(
            1L, 
            "Generated Test Trip", 
            TEST_DATE_START, 
            TEST_DATE_END_VALID, 
            START_LAT_VALID, 
            START_LON_VALID, 
            END_LAT_VALID, 
            END_LON_VALID, 
            false, 
            MAX_HOURS_PER_DAY, 
            1L, 
            null
        );

        VALID_TRIP_GEN_INPUT = new Trip();
        VALID_TRIP_GEN_INPUT.setId(1L); 
        VALID_TRIP_GEN_INPUT.setName("Generated Test Trip");
        VALID_TRIP_GEN_INPUT.setStartDate(TEST_DATE_START);
        VALID_TRIP_GEN_INPUT.setEndDate(TEST_DATE_END_VALID);
        VALID_TRIP_GEN_INPUT.setStartLatitude(START_LAT_VALID);
        VALID_TRIP_GEN_INPUT.setStartLongitude(START_LON_VALID);
        VALID_TRIP_GEN_INPUT.setEndLatitude(END_LAT_VALID);
        VALID_TRIP_GEN_INPUT.setEndLongitude(END_LON_VALID);
        VALID_TRIP_GEN_INPUT.setIsGenerated(false);
        VALID_TRIP_GEN_INPUT.setMaxHoursPerDay(MAX_HOURS_PER_DAY);
        VALID_TRIP_GEN_INPUT.setUserId(1L);

        TripStop VALID_TRIP_STOP_START = new TripStop();
        VALID_TRIP_STOP_START.setId(1L);
        VALID_TRIP_STOP_START.setDate(Date.from(LocalDate.of(2026, 3, 25).atStartOfDay(ZoneId.of("UTC")).toInstant()));
        VALID_TRIP_STOP_START.setLocation("Start");
        VALID_TRIP_STOP_START.setBallparkId(null);
        VALID_TRIP_STOP_START.setGameId(null);

        TripStop VALID_TRIP_STOP_SD = new TripStop();
        VALID_TRIP_STOP_SD.setId(2L);
        VALID_TRIP_STOP_SD.setDate(Date.from(LocalDate.of(2026, 3, 26).atStartOfDay(ZoneId.of("UTC")).toInstant()));
        VALID_TRIP_STOP_SD.setLocation("San Diego");
        VALID_TRIP_STOP_SD.setBallparkId(2680);
        VALID_TRIP_STOP_SD.setGameId("1773b103-88ec-4b64-8163-5fb06b605339");

        TripStop VALID_TRIP_STOP_LA = new TripStop();
        VALID_TRIP_STOP_LA.setId(3L);
        VALID_TRIP_STOP_LA.setDate(Date.from(LocalDate.of(2026, 3, 27).atStartOfDay(ZoneId.of("UTC")).toInstant()));
        VALID_TRIP_STOP_LA.setLocation("Los Angeles");
        VALID_TRIP_STOP_LA.setBallparkId(22);
        VALID_TRIP_STOP_LA.setGameId("70ca6bd1-99b7-4986-bf4a-ea0fa6d08d2c");

        TripStop VALID_TRIP_STOP_SF = new TripStop();
        VALID_TRIP_STOP_SF.setId(4L);
        VALID_TRIP_STOP_SF.setDate(Date.from(LocalDate.of(2026, 3, 28).atStartOfDay(ZoneId.of("UTC")).toInstant()));
        VALID_TRIP_STOP_SF.setLocation("San Francisco");
        VALID_TRIP_STOP_SF.setBallparkId(2395);
        VALID_TRIP_STOP_SF.setGameId("0d3c3efc-80cb-4323-a44c-3e6d40555080");

        TripStop VALID_TRIP_STOP_END = new TripStop();
        VALID_TRIP_STOP_END.setId(5L);
        VALID_TRIP_STOP_END.setDate(Date.from(LocalDate.of(2026, 3, 29).atStartOfDay(ZoneId.of("UTC")).toInstant()));
        VALID_TRIP_STOP_END.setLocation("End");
        VALID_TRIP_STOP_END.setBallparkId(null);
        VALID_TRIP_STOP_END.setGameId(null);

        VALID_TRIP_GENERATED = new Trip();
        VALID_TRIP_GENERATED.setId(1L);
        VALID_TRIP_GENERATED.setName("Generated Test Trip");
        VALID_TRIP_GENERATED.setStartDate(TEST_DATE_START);
        VALID_TRIP_GENERATED.setEndDate(TEST_DATE_END_VALID);
        VALID_TRIP_GENERATED.setStartLatitude(START_LAT_VALID);
        VALID_TRIP_GENERATED.setStartLongitude(START_LON_VALID);
        VALID_TRIP_GENERATED.setEndLatitude(END_LAT_VALID);
        VALID_TRIP_GENERATED.setEndLongitude(END_LON_VALID);
        VALID_TRIP_GENERATED.setIsGenerated(true);
        VALID_TRIP_GENERATED.setMaxHoursPerDay(MAX_HOURS_PER_DAY);
        VALID_TRIP_GENERATED.setUserId(1L);
        List<TripStop> stops = List.of(
            VALID_TRIP_STOP_START,
            VALID_TRIP_STOP_SD,
            VALID_TRIP_STOP_LA,
            VALID_TRIP_STOP_SF,
            VALID_TRIP_STOP_END
        );
        for (TripStop stop : stops) {
            stop.setTrip(VALID_TRIP_GENERATED);
            VALID_TRIP_GENERATED.addTripStop(stop);
        }
    }

}
