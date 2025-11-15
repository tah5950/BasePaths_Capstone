package psu.basepaths.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.lang.Collections;
import psu.basepaths.model.Trip;
import psu.basepaths.model.TripStop;
import psu.basepaths.model.dto.BallparkDTO;
import psu.basepaths.model.dto.GameDTO;
import psu.basepaths.model.dto.TripDTO;
import psu.basepaths.model.dto.TripStopDTO;
import psu.basepaths.repository.TripRepository;
import psu.basepaths.utilities.TripUtilities;

@Service
public class TripServiceImpl implements TripService {
    private final TripRepository tripRepository;
    private final BallparkService ballparkService;
    private final GameService gameService;

    public TripServiceImpl(TripRepository tripRepository,
                           BallparkService ballparkService,
                           GameService gameService){
        this.tripRepository = tripRepository;
        this.ballparkService = ballparkService;
        this.gameService = gameService;
    }

    @Override
    public TripDTO createTrip(TripDTO tripDTO) {
        validateTrip(tripDTO);

        Trip trip = convertToEntity(tripDTO);

        Trip createdTrip = tripRepository.save(trip);

        return convertToDTO(createdTrip);
    }

    @Override
    public List<TripDTO> getUserTrips(Long userid) {
        return tripRepository.findByUserId(userid)
            .orElse(Collections.emptyList())
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public TripDTO updateTrip(TripDTO tripDTO, boolean generateUpdate) {
        validateTrip(tripDTO);

        Trip existingTrip = tripRepository.findById(tripDTO.tripId())
            .orElseThrow(() -> new RuntimeException("Trip not found with id " + tripDTO.tripId()));

        existingTrip.setName(tripDTO.name());
        existingTrip.setStartDate(tripDTO.startDate());
        existingTrip.setEndDate(tripDTO.endDate());
        existingTrip.setUserId(tripDTO.userId());

        //Update trip stops
        existingTrip.getTripStops().clear();

        if(generateUpdate){
            existingTrip.setStartLatitude(tripDTO.startLatitude());
            existingTrip.setStartLongitude(tripDTO.startLongitude());
            existingTrip.setEndLatitude(tripDTO.endLatitude());
            existingTrip.setEndLongitude(tripDTO.endLongitude());
            existingTrip.setIsGenerated(tripDTO.isGenerated());
            existingTrip.setMaxHoursPerDay(tripDTO.maxHoursPerDay());

            if(tripDTO.tripStops() != null) {
            List<TripStop> newStops = tripDTO.tripStops().stream()
                .map(stopDto -> {
                    TripStop stop = new TripStop();
                    stop.setId(stopDto.tripStopId());
                    stop.setDate(stopDto.date());
                    stop.setLocation(stopDto.location());
                    stop.setBallparkId(stopDto.ballparkId());
                    stop.setGameId(stopDto.gameId());
                    stop.setTrip(existingTrip);
                    return stop;
                }).collect(Collectors.toList());
                existingTrip.getTripStops().addAll(newStops);
            }
        } else {
            existingTrip.setStartLatitude(null);
            existingTrip.setStartLongitude(null);
            existingTrip.setEndLatitude(null);
            existingTrip.setEndLongitude(null);
            existingTrip.setIsGenerated(false);
            existingTrip.setMaxHoursPerDay(null);
        }

        Trip updatedTrip = tripRepository.save(existingTrip);
        return convertToDTO(updatedTrip);
    }

    @Override
    public void deleteTrip(Long tripid, Long userid){
        Trip trip = tripRepository.findByIdAndUserId(tripid, userid)
            .orElseThrow(() -> new RuntimeException("Trip not found or not accessible by user: " + userid));

        tripRepository.delete(trip);
    }

    @Override
    public TripDTO getTripById(Long tripid, Long userid) {
        Trip trip = tripRepository.findByIdAndUserId(tripid, userid)
            .orElseThrow(() -> new RuntimeException("Trip not found or not accessible by user: " + userid));

        return convertToDTO(trip);
    }

    @Override
    public TripDTO generateTrip(TripDTO trip){
        validateGeneratingTrip(trip);

        TripUtilities tripUtils = new TripUtilities();

        List<BallparkDTO> ballparks = ballparkService.getAllBallparks();
        List<GameDTO> gamesByDates = gameService.getGameByDateRange(trip.startDate(), trip.endDate());

        TripDTO generatedTrip = tripUtils.generateTrip(trip, ballparks, gamesByDates);

        updateTrip(generatedTrip, true);

        return generatedTrip;
    }

    private void validateTrip(TripDTO tripDTO){
        if(tripDTO.name().isEmpty()){
            throw new IllegalArgumentException("Name must not be blank");
        }
        if(tripDTO.startDate() == null) {
            throw new IllegalArgumentException("Start Date must not be empty");
        }
        if(tripDTO.endDate() == null) {
            throw new IllegalArgumentException("End Date must not be empty");
        }
        if(tripDTO.startDate().after(tripDTO.endDate())){
            throw new IllegalArgumentException("Start Date must be before End Date");
        }
        if(TripUtilities.getDaysBetween(tripDTO.startDate(), tripDTO.endDate()) > 14){
            throw new IllegalArgumentException("Only trips of 2 weeks or less are supported currently");
        }
    }

    private void validateGeneratingTrip(TripDTO tripDTO){
        if(tripDTO.startLatitude() == null || tripDTO.startLatitude() > 90 || tripDTO.startLatitude() < -90 ){
            throw new IllegalArgumentException("Invalid Start Latitude");
        }
        if(tripDTO.startLongitude() == null || tripDTO.startLongitude() > 180 || tripDTO.startLongitude() < -180 ) {
            throw new IllegalArgumentException("Invalid Start Longitude");
        }
        if(tripDTO.endLatitude() == null || tripDTO.endLatitude() > 90 || tripDTO.endLatitude() < -90 ){
            throw new IllegalArgumentException("Invalid End Latitude");
        }
        if(tripDTO.endLongitude() == null || tripDTO.endLongitude() > 180 || tripDTO.endLongitude() < -180 ) {
            throw new IllegalArgumentException("Invalid End Longitude");
        }
        if(tripDTO.maxHoursPerDay() == null) {
            throw new IllegalArgumentException("Max Hours Per Day must not be empty");
        }
    }
    
    private Trip convertToEntity(TripDTO tripDTO) {
        Trip trip = new Trip();
        trip.setId(tripDTO.tripId());
        trip.setName(tripDTO.name());
        trip.setStartDate(tripDTO.startDate());
        trip.setEndDate(tripDTO.endDate());
        trip.setStartLatitude(tripDTO.startLatitude());
        trip.setStartLongitude(tripDTO.startLongitude());
        trip.setEndLatitude(tripDTO.endLatitude());
        trip.setEndLongitude(tripDTO.endLongitude());
        trip.setIsGenerated(tripDTO.isGenerated());
        trip.setMaxHoursPerDay(tripDTO.maxHoursPerDay());
        trip.setUserId(tripDTO.userId());

        if (tripDTO.tripStops() != null) {
            List<TripStop> stops = tripDTO.tripStops()
                .stream()
                .map(stopDto -> {
                    TripStop stop = new TripStop();
                    stop.setId(stopDto.tripStopId());
                    stop.setDate(stopDto.date());
                    stop.setLocation(stopDto.location());
                    stop.setBallparkId(stopDto.ballparkId());
                    stop.setGameId(stopDto.gameId());
                    stop.setTrip(trip);
                    return stop;
                }).collect(Collectors.toList());
            trip.setTripStops(stops);
        }

        return trip;
    }

    private TripDTO convertToDTO(Trip trip){
        List<TripStopDTO> tripStopDTOs = trip.getTripStops()
            .stream()
            .map(stop -> {
                TripStopDTO stopDTO = new TripStopDTO(
                    stop.getId(),
                    stop.getDate(),
                    stop.getLocation(),
                    stop.getBallparkId(),
                    stop.getGameId()
                );
                return stopDTO;
            }).collect(Collectors.toList());
        
        TripDTO tripDTO = new TripDTO(
            trip.getId(),
            trip.getName(),
            trip.getStartDate(),
            trip.getEndDate(),
            trip.getStartLatitude(),
            trip.getStartLongitude(),
            trip.getEndLatitude(),
            trip.getEndLongitude(),
            trip.getIsGenerated(),
            trip.getMaxHoursPerDay(),
            trip.getUserId(),
            tripStopDTOs
        );
        

        return tripDTO;
    }
}
