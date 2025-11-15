package psu.basepaths.service;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import psu.basepaths.model.Game;
import psu.basepaths.model.Trip;
import psu.basepaths.model.TripStop;
import psu.basepaths.model.dto.TripDTO;
import psu.basepaths.model.dto.TripStopDTO;
import psu.basepaths.repository.BallparkRepository;
import psu.basepaths.repository.GameRepository;
import psu.basepaths.repository.TripRepository;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {
    @Mock
    private GameRepository gameRepository;

    @Mock
    private BallparkRepository ballparkRepository;

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private TripServiceImpl tripService;

    private static final double START_LAT_VALID = 32.72;
    private static final double START_LON_VALID = -117.16;
    private static final double END_LAT_VALID = 38.58;
    private static final double END_LON_VALID = -121.49;

    private static int MAX_HOURS_PER_DAY = 8;

    private static Date TEST_DATE_START;
    private static Date TEST_DATE_END_VALID;
    private static Date TEST_DATE_END_INVALID;

    private static Trip VALID_TRIP_1;
    private static Trip VALID_TRIP_2_NO_STOPS;

    private static TripStop VALID_TRIP_STOP_1;
    private static TripStop VALID_TRIP_STOP_2;

    private static TripDTO VALID_TRIP_DTO_1;

    private static TripStopDTO VALID_TRIP_STOP_DTO_1;
    private static TripStopDTO VALID_TRIP_STOP_DTO_2;

    @BeforeAll
    public static void setUp() {
        LocalDate localDateStart = LocalDate.of(2026, 3, 25);
        TEST_DATE_START = Date.from(localDateStart.atStartOfDay(ZoneId.of("UTC")).toInstant());

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
        VALID_TRIP_1.setTripStops(List.of(VALID_TRIP_STOP_1, VALID_TRIP_STOP_2));

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

    // BUT TBD - Create Trip Longer than 2 Weeks
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
}
